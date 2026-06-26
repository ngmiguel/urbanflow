package com.urbanflow.environment.application.dto;

import java.time.Instant;
import java.util.UUID;

public record SensorReadingDto(
        UUID id,
        String deviceId,
        String sensorType,
        double value,
        String unit,
        String zoneId,
        double latitude,
        double longitude,
        String sourceEventId,
        Instant recordedAt
) {
}
