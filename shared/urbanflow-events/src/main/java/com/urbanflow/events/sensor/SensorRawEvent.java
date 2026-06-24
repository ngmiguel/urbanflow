package com.urbanflow.events.sensor;

import com.urbanflow.events.DomainEvent;
import com.urbanflow.events.EventMetadata;

/**
 * Raw sensor reading published on {@code urbanflow.sensor.raw} (Phase 2).
 */
public record SensorRawEvent(
        EventMetadata metadata,
        String deviceId,
        String sensorType,
        double value,
        String unit,
        double latitude,
        double longitude,
        String zoneId
) implements DomainEvent {
}
