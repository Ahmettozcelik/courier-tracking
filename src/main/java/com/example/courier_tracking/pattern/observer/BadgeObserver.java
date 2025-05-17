package com.example.courier_tracking.pattern.observer;

import com.example.courier_tracking.entity.StoreVisit;
import com.example.courier_tracking.repository.StoreVisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.courier_tracking.util.DateTimeUtil.*;


public class BadgeObserver implements VisitObserver {

    private static final Logger logger = LoggerFactory.getLogger(BadgeObserver.class);
    private final StoreVisitRepository storeVisitRepository;

    public BadgeObserver(StoreVisitRepository storeVisitRepository) {
        this.storeVisitRepository = storeVisitRepository;
    }

    @Override
    public void onStoreVisit(String courierId, String storeName, LocalDateTime time) {

        Set<String> distinctStores = storeVisitRepository.findByCourierIdOrderByEntryTimeAsc(courierId).stream()
                .map(StoreVisit::getStoreName)
                .collect(Collectors.toSet());

        if (distinctStores.size() == 3) {
            logger.info("🏅 Courier {} visited 3 different stores — Achievement unlocked as of {}!", courierId, format(time));
        }
    }
}
