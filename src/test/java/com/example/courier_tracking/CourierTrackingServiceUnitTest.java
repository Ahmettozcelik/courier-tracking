package com.example.courier_tracking;

import com.example.courier_tracking.entity.CourierLocation;
import com.example.courier_tracking.entity.Store;
import com.example.courier_tracking.entity.StoreVisit;
import com.example.courier_tracking.repository.CourierLocationRepository;
import com.example.courier_tracking.repository.StoreVisitRepository;
import com.example.courier_tracking.service.CourierTrackingService;
import com.example.courier_tracking.service.StoreService;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourierTrackingServiceUnitTest {

    private CourierTrackingService service;
    private StoreService mockStoreService;
    private CourierLocationRepository mockLocationRepo;
    private StoreVisitRepository mockVisitRepo;

    @BeforeEach
    void setup() {
        mockStoreService = mock(StoreService.class);
        mockLocationRepo = mock(CourierLocationRepository.class);
        mockVisitRepo = mock(StoreVisitRepository.class);

        service = new CourierTrackingService(mockStoreService, mockVisitRepo, mockLocationRepo);
    }


    @Test()
    @Order(1)
    void testCourierFirstVisitShouldBeSaved() {

        Store store = new Store("Ataşehir MMM Migros", 40.9923307, 29.1244229);
        when(mockStoreService.getAllStores()).thenReturn(List.of(store));

        CourierLocation location = new CourierLocation("1", 40.9923307, 29.1244229, LocalDateTime.now());

        service.receiveLocation(location);

        verify(mockVisitRepo, times(1)).save(any(StoreVisit.class));
    }

    @Test()
    @Order(2)
    void testCourierSecondVisitWithinOneMinuteShouldNotBeSaved() {

        Store store = new Store("Ataşehir MMM Migros", 40.9923307, 29.1244229);
        when(mockStoreService.getAllStores()).thenReturn(List.of(store));

        LocalDateTime now = LocalDateTime.now();

        CourierLocation firstVisit = new CourierLocation("1", 40.9923307, 29.1244229, now);
        CourierLocation secondVisit = new CourierLocation("1", 40.9923307, 29.1244229, now.plusSeconds(30));


        service.receiveLocation(firstVisit);
        service.receiveLocation(secondVisit); // already in store

        verify(mockVisitRepo, times(1)).save(any(StoreVisit.class));
    }
}
