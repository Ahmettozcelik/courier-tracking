package com.example.courier_tracking;

import com.example.courier_tracking.entity.CourierLocation;
import com.example.courier_tracking.service.CourierTrackingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class CourierTrackingApplicationTests {

	@Autowired
	private CourierTrackingService courierTrackingService;

	@Test
	void contextLoads() {

		assertDoesNotThrow(() -> {
			assert courierTrackingService != null;
		});
	}

	@Test
	void receiveLocation_doesNotFail() {
		CourierLocation location = new CourierLocation();
		location.setCourierId("1");
		location.setLat(40.992);
		location.setLng(29.124);
		location.setTimestamp(LocalDateTime.now());

		assertDoesNotThrow(() -> courierTrackingService.receiveLocation(location));
	}
}
