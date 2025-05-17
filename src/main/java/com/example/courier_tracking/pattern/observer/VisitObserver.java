package com.example.courier_tracking.pattern.observer;

import java.time.LocalDateTime;

public interface VisitObserver {
    void onStoreVisit(String courierId, String storeName, LocalDateTime time);
}
