package com.example.courier_tracking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "store_visit")
public class StoreVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courierId;
    private String storeName;
    private LocalDateTime entryTime;

    public StoreVisit() {
    }

    public StoreVisit(String courierId, String storeName, LocalDateTime entryTime) {
        this.courierId = courierId;
        this.storeName = storeName;
        this.entryTime = entryTime;
    }
}
