package com.garvk.aiservice.exception;

import lombok.Getter;

/**
 * Thrown when an inter-service REST call fails.
 */
@Getter
public class ServiceClientException extends RuntimeException {

    private final String serviceName;
    private final int statusCode;

    public ServiceClientException(String serviceName, int statusCode, String message) {
        super(message);
        this.serviceName = serviceName;
        this.statusCode = statusCode;
    }

    public ServiceClientException(String serviceName, String message, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
        this.statusCode = 503;
    }
}
