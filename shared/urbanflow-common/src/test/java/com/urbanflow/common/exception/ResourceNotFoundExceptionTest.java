package com.urbanflow.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldFormatResourceMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("IoTDevice", "sensor-01");

        assertEquals("RESOURCE_NOT_FOUND", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("IoTDevice"));
        assertTrue(exception.getMessage().contains("sensor-01"));
    }
}
