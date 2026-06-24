package com.urbanflow.common.dto;

import java.time.Instant;
import java.util.List;

/**
 * RFC 7807-inspired error payload for consistent error responses.
 */
public record ErrorResponse(
        String code,
        String message,
        String path,
        Instant timestamp,
        String correlationId,
        List<String> details
) {

    public static ErrorResponse of(
            String code,
            String message,
            String path,
            String correlationId
    ) {
        return new ErrorResponse(code, message, path, Instant.now(), correlationId, List.of());
    }

    public static ErrorResponse of(
            String code,
            String message,
            String path,
            String correlationId,
            List<String> details
    ) {
        return new ErrorResponse(code, message, path, Instant.now(), correlationId, List.copyOf(details));
    }
}
