package com.garvk.aiservice.client;

import com.garvk.aiservice.exception.ServiceClientException;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Abstract base for typed inter-service REST clients.
 * <p>
 * Subclasses pass the target service name (as registered in Eureka).
 * The base class builds a {@link RestClient} pointing at {@code http://{serviceName}}
 * with a {@link LoadBalancerInterceptor} for Eureka-based service resolution.
 * <p>
 * <strong>Important:</strong> We build the RestClient inline with a manually-created
 * interceptor instead of using a Spring-managed {@code RestClient.Builder} bean.
 * Spring Cloud 2025 auto-applies load-balancer interceptors to ALL RestClient.Builder
 * beans in the context, which contaminates Eureka's own internal RestClient and
 * causes a circular {@code BeanCurrentlyInCreationException} at startup.
 */
public abstract class BaseServiceClient {

    protected final String serviceName;
    protected final RestClient restClient;

    protected BaseServiceClient(String serviceName, LoadBalancerClient loadBalancerClient) {
        this.serviceName = serviceName;
        this.restClient = RestClient.builder()
                .baseUrl("http://" + serviceName)
                .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
                .build();
    }

    /**
     * Perform a GET request and return the response body as a {@code Map<String, Object>}.
     *
     * @param path relative URI on the target service (e.g. "/internal/summary")
     * @return deserialized JSON map
     * @throws ServiceClientException on any non-2xx response or connectivity error
     */
    protected Map<String, Object> get(String path) {
        try {
            return restClient.get()
                    .uri(path)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new ServiceClientException(
                                serviceName,
                                response.getStatusCode().value(),
                                "Call to " + serviceName + " " + path + " failed with status " + response.getStatusCode()
                        );
                    })
                    .body(new ParameterizedTypeReference<>() {});
        } catch (ServiceClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceClientException(
                    serviceName,
                    "Failed to reach " + serviceName + ": " + e.getMessage(),
                    e
            );
        }
    }
}
