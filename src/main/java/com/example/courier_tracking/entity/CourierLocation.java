package com.example.courier_tracking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "courier_location")
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courierId;
    private double lat;
    private double lng;
    private LocalDateTime timestamp;

    public CourierLocation(String courierId, double lat, double lng, LocalDateTime timestamp) {
        this.courierId = courierId;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }
}
