package com.garvk.aiservice.controller;

import com.garvk.aiservice.service.HealthDataAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/aggregate")
@RequiredArgsConstructor
public class AggregateController {

    private final HealthDataAggregator healthDataAggregator;

    /**
     * Demo endpoint: fetches summary data from all domain services
     * and returns a unified health data response.
     */
    @GetMapping("/health-summary")
    public ResponseEntity<Map<String, Object>> getHealthSummary() {
        return ResponseEntity.ok(healthDataAggregator.aggregateHealthSummary());
    }
}
