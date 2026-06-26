package com.urbanflow.analytics.presentation.dto;

import com.urbanflow.analytics.domain.model.MetricHistoryEntry;

public record MetricHistoryResponse(
        String id,
        String zoneId,
        String metricType,
        double value,
        String recordedAt
) {

    public static MetricHistoryResponse from(MetricHistoryEntry entry) {
        return new MetricHistoryResponse(
                entry.id().toString(),
                entry.zoneId(),
                entry.metricType().name(),
                entry.value(),
                entry.recordedAt().toString()
        );
    }
}
