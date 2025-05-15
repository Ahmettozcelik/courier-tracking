package com.example.courier_tracking.entity.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DistanceResponse {
    private String courierId;
    private double totalDistanceKm;
    private String message;

    public DistanceResponse(String courierId, double totalDistanceKm) {
        this.courierId = courierId;
        this.totalDistanceKm = Math.round(totalDistanceKm * 100.0) / 100.0;
        this.message = "Total distance traveled is " + this.totalDistanceKm + " km";
    }

}
