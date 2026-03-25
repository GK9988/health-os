package com.garvk.aiservice.client;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SupplementServiceClient extends BaseServiceClient {

    public SupplementServiceClient(LoadBalancerClient loadBalancerClient) {
        super("supplement-service", loadBalancerClient);
    }

    public Map<String, Object> getSummary() {
        return get("/internal/summary");
    }
}
