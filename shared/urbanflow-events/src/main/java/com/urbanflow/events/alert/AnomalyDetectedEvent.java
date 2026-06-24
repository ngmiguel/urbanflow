package com.urbanflow.events.alert;

import com.urbanflow.events.DomainEvent;
import com.urbanflow.events.EventMetadata;

/**
 * Anomaly detected by streaming rules, published on {@code urbanflow.anomaly.detected} (Phase 2+).
 */
public record AnomalyDetectedEvent(
        EventMetadata metadata,
        String anomalyType,
        double score,
        String zoneId,
        String deviceId,
        String description
) implements DomainEvent {
}
