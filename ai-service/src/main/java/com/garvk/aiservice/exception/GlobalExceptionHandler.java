package com.garvk.aiservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceClientException.class)
    public ResponseEntity<Map<String, Object>> handleServiceClientException(ServiceClientException ex) {
        Map<String, Object> body = Map.of(
                "error", "SERVICE_CALL_FAILED",
                "targetService", ex.getServiceName(),
                "statusCode", ex.getStatusCode(),
                "message", ex.getMessage(),
                "timestamp", Instant.now().toString()
        );

        HttpStatus status = ex.getStatusCode() >= 500
                ? HttpStatus.BAD_GATEWAY
                : HttpStatus.valueOf(ex.getStatusCode());

        return new ResponseEntity<>(body, status);
    }
}
