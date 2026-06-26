package com.urbanflow.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionTest {

    @Test
    void shouldExposeErrorCodeAndMessage() {
        BusinessException exception = new BusinessException("DEVICE_OFFLINE", "Device must be online");

        assertEquals("DEVICE_OFFLINE", exception.getErrorCode());
        assertEquals("Device must be online", exception.getMessage());
    }
}
