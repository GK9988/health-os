package com.garvk.aiservice.client;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NutritionServiceClient extends BaseServiceClient {

    public NutritionServiceClient(LoadBalancerClient loadBalancerClient) {
        super("nutrition-service", loadBalancerClient);
    }

    public Map<String, Object> getSummary() {
        return get("/internal/summary");
    }
}
