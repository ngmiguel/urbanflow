package com.urbanflow.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiResponseTest {

    @Test
    void shouldCreateSuccessResponse() {
        ApiResponse<String> response = ApiResponse.ok("urbanflow");

        assertTrue(response.success());
        assertNotNull(response.data());
        assertNotNull(response.timestamp());
    }

    @Test
    void shouldCreateErrorResponse() {
        ApiResponse<Void> response = ApiResponse.error("Something went wrong");

        assertFalse(response.success());
        assertNotNull(response.message());
    }
}
