package org.buildATrip.controller;

import org.buildATrip.service.AmadeusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestConnectionController {

    private final AmadeusServiceImpl amadeusService;

    @Autowired
    public TestConnectionController(AmadeusServiceImpl amadeusService) {
        this.amadeusService = amadeusService;
    }

    @GetMapping("/amadeus-connection")
    public ResponseEntity<Map<String, Object>> testAmadeusConnection() {
        Map<String, Object> response = new HashMap<>();
        boolean connectionSuccessful = amadeusService.testAmadeusConnection();

        response.put("connection_successful", connectionSuccessful);
        response.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(response,
                connectionSuccessful ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE);
    }
}