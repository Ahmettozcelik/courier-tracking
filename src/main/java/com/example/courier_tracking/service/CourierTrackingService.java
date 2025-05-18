package com.example.courier_tracking.service;

import com.example.courier_tracking.entity.CourierLocation;
import com.example.courier_tracking.entity.Store;
import com.example.courier_tracking.entity.StoreVisit;
import com.example.courier_tracking.entity.dto.VisitResponse;
import com.example.courier_tracking.exception.CourierNotFoundException;
import com.example.courier_tracking.pattern.observer.BadgeObserver;
import com.example.courier_tracking.pattern.observer.VisitObserver;
import com.example.courier_tracking.repository.CourierLocationRepository;
import com.example.courier_tracking.repository.StoreVisitRepository;
import com.example.courier_tracking.util.Haversine;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.courier_tracking.util.DateTimeUtil.*;
import static com.example.courier_tracking.util.DateTimeUtil.format;

@Service
public class CourierTrackingService {

    private static final Logger logger = LoggerFactory.getLogger(CourierTrackingService.class);

    private final StoreService storeService;
    private final StoreVisitRepository storeVisitRepository;
    private final CourierLocationRepository locationRepository;

    private final List<VisitObserver> observers = new ArrayList<>();
    private final Map<String, Map<String, LocalDateTime>> lastEntryTime = new HashMap<>();

    public CourierTrackingService(StoreService storeService, StoreVisitRepository storeVisitRepository, CourierLocationRepository locationRepository) {
        this.storeService = storeService;
        this.storeVisitRepository = storeVisitRepository;
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    public void initObservers() {
        this.addObserver(new BadgeObserver(storeVisitRepository));
    }

    //Lokasyon güncellemesi alındığında hem kayıt yapar, hem mağaza kontrolü yapar
    public void receiveLocation(CourierLocation location) {
        String courierId = location.getCourierId();
        LocalDateTime timestamp = location.getTimestamp();

        // Zaman kontrolü: son kayıttan eskiyse hiçbir şey yapma
        LocalDateTime lastVisitTime = storeVisitRepository
                .findByCourierIdOrderByEntryTimeDesc(courierId).stream()
                .findFirst()
                .map(StoreVisit::getEntryTime)
                .orElse(null);

        if (lastVisitTime != null && timestamp.isBefore(lastVisitTime)) {
            logger.warn("⛔ Courier {} sent a timestamp earlier than last visit ({} < {}). Location ignored.",
                    courierId, format(timestamp), format(lastVisitTime));
            return;
        }

        // ✅ Geçerli kayıt: hem konumu hem mağaza ziyaretini işle
        locationRepository.save(location);
        checkStoreProximity(location);
    }

    public double getTotalTravelDistance(String courierId) {
        List<CourierLocation> locations = locationRepository.findByCourierIdOrderByTimestampAsc(courierId);

        if (locations.isEmpty()) {
            throw new CourierNotFoundException("Courier with ID '" + courierId + "' not found.");
        }

        if (locations.size() < 2) return 0.0;

        double total = 0.0;
        for (int i = 1; i < locations.size(); i++) {
            CourierLocation prev = locations.get(i - 1);
            CourierLocation curr = locations.get(i);
            total += Haversine.distance(prev.getLat(), prev.getLng(), curr.getLat(), curr.getLng());
        }
        return total;
    }

    //Belirli bir kurye için mağaza ziyaretlerini listeler
    public List<VisitResponse> getVisitsByCourierId(String courierId) {
        List<StoreVisit> visits = storeVisitRepository.findByCourierIdOrderByEntryTimeAsc(courierId);

        if (visits.isEmpty()) {
            throw new CourierNotFoundException("Courier with ID '" + courierId + "' not found or has no visits.");
        }

        return visits.stream()
                .map(v -> new VisitResponse(v.getStoreName(), v.getEntryTime()))
                .toList();
    }


    //Kurye herhangi bir mağazaya 100 metre içinde mi? 1 dakikadan fazla geçti mi? kontrol edilir
    private void checkStoreProximity(CourierLocation location) {
        String courierId = location.getCourierId();
        LocalDateTime timestamp = location.getTimestamp();

        boolean visitedAnyStore = false;

        for (Store store : storeService.getAllStores()) {
            double distance = Haversine.distance(
                    location.getLat(), location.getLng(),
                    store.getLat(), store.getLng()
            );

            if (distance <= 0.1) { // 100 metre

                Map<String, LocalDateTime> lastEntries = lastEntryTime
                        .computeIfAbsent(courierId, k -> new HashMap<>());

                LocalDateTime lastTime = lastEntries.get(store.getName());

                if (lastTime == null || Duration.between(lastTime, timestamp).toMinutes() >= 1) {
                    lastEntries.put(store.getName(), timestamp);
                    StoreVisit visit = new StoreVisit(courierId, store.getName(), timestamp);
                    storeVisitRepository.save(visit);

                    logger.info("Courier {} entered store {} at {}", courierId, store.getName(), format(timestamp));
                    notifyObservers(courierId, store.getName(), timestamp);
                    visitedAnyStore = true;
                } else {
                    logger.info("Courier {} already in store {} (last entered at {})",
                            courierId, store.getName(), format(lastTime));
                    visitedAnyStore = true;
                }
            }
        }
        if (!visitedAnyStore) {
            logger.info("📍 Courier {} is traveling, not near any store at {}", courierId, format(timestamp));
        }
    }

    public void addObserver(VisitObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(String courierId, String storeName, LocalDateTime time) {
        for (VisitObserver observer : observers) {
            observer.onStoreVisit(courierId, storeName, time);
        }
    }
}
