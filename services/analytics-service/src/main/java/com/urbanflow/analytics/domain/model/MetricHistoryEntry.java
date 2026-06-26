package com.urbanflow.analytics.domain.model;

import java.time.Instant;
import java.util.UUID;

public record MetricHistoryEntry(
        UUID id,
        String zoneId,
        MetricType metricType,
        double value,
        Instant recordedAt
) {
}
