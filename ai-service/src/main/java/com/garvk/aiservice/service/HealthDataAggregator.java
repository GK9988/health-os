package com.garvk.aiservice.service;

import com.garvk.aiservice.client.NutritionServiceClient;
import com.garvk.aiservice.client.SleepServiceClient;
import com.garvk.aiservice.client.SupplementServiceClient;
import com.garvk.aiservice.client.WorkoutServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Orchestrates inter-service calls and aggregates health data
 * from all domain services. This is the integration point where
 * future AI processing logic will be plugged in.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthDataAggregator {

    private final WorkoutServiceClient workoutClient;
    private final SleepServiceClient sleepClient;
    private final NutritionServiceClient nutritionClient;
    private final SupplementServiceClient supplementClient;

    /**
     * Fetches summary data from all domain services and returns
     * a unified view of the user's health data.
     */
    public Map<String, Object> aggregateHealthSummary() {
        log.info("Aggregating health data from all domain services");

        Map<String, Object> aggregated = new LinkedHashMap<>();
        aggregated.put("timestamp", Instant.now().toString());

        aggregated.put("workout", workoutClient.getSummary());
        aggregated.put("sleep", sleepClient.getSummary());
        aggregated.put("nutrition", nutritionClient.getSummary());
        aggregated.put("supplement", supplementClient.getSummary());

        log.info("Health data aggregation complete");
        return aggregated;
    }
}
