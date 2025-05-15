package com.example.courier_tracking.controller;

import com.example.courier_tracking.entity.CourierLocation;
import com.example.courier_tracking.entity.dto.DistanceResponse;
import com.example.courier_tracking.entity.dto.VisitResponse;
import com.example.courier_tracking.service.CourierTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
public class CourierController {

    private final CourierTrackingService trackingService;

    public CourierController(CourierTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PostMapping("/location")
    public ResponseEntity<Void> updateLocation(@RequestBody CourierLocation location) {
        trackingService.receiveLocation(location);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courierId}/distance")
    public ResponseEntity<DistanceResponse> getTotalDistance(@PathVariable String courierId) {
        double distance = trackingService.getTotalTravelDistance(courierId);
        DistanceResponse response = new DistanceResponse(courierId, distance);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courierId}/visits")
    public ResponseEntity<List<VisitResponse>> getCourierVisits(@PathVariable String courierId) {
        List<VisitResponse> visits = trackingService.getVisitsByCourierId(courierId);
        return ResponseEntity.ok(visits);
    }

}
