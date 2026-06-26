package com.urbanflow.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorResponseTest {

    @Test
    void shouldCreateBasicErrorResponse() {
        ErrorResponse response = ErrorResponse.of(
                "VALIDATION_ERROR",
                "Invalid payload",
                "/api/v1/traffic",
                "corr-42"
        );

        assertEquals("VALIDATION_ERROR", response.code());
        assertEquals("Invalid payload", response.message());
        assertEquals("/api/v1/traffic", response.path());
        assertEquals("corr-42", response.correlationId());
        assertNotNull(response.timestamp());
        assertTrue(response.details().isEmpty());
    }

    @Test
    void shouldCreateErrorResponseWithDetails() {
        ErrorResponse response = ErrorResponse.of(
                "VALIDATION_ERROR",
                "Invalid payload",
                "/api/v1/traffic",
                "corr-42",
                java.util.List.of("field: required")
        );

        assertEquals(1, response.details().size());
        assertEquals("field: required", response.details().getFirst());
    }
}
