package com.garvk.supplementservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Internal endpoints used for inter-service communication.
 * These are called by the AI service and are NOT exposed
 * through the API Gateway.
 */
@RestController
@RequestMapping("/internal")
public class InternalController {

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        // TODO: Replace with real data from service/repository layer
        Map<String, Object> summary = Map.of(
                "service", "supplement-service",
                "activeSupplements", 5,
                "dailyDosesCompleted", 3,
                "status", "active"
        );
        return ResponseEntity.ok(summary);
    }
}
