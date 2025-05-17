package com.example.courier_tracking.repository;

import com.example.courier_tracking.entity.StoreVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreVisitRepository extends JpaRepository<StoreVisit, Long> {
    List<StoreVisit> findByCourierIdOrderByEntryTimeAsc(String courierId);
    List<StoreVisit> findByCourierIdOrderByEntryTimeDesc(String courierId);
}
