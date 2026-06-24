package com.urbanflow.common.dto;

/**
 * Pagination metadata for list endpoints.
 */
public record PageMeta(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
