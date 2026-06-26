package com.urbanflow.environment.presentation.dto;

import com.urbanflow.environment.application.dto.SensorReadingDto;

import java.time.Instant;
import java.util.UUID;

public record SensorReadingResponse(
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

    public static SensorReadingResponse from(SensorReadingDto dto) {
        return new SensorReadingResponse(
                dto.id(),
                dto.deviceId(),
                dto.sensorType(),
                dto.value(),
                dto.unit(),
                dto.zoneId(),
                dto.latitude(),
                dto.longitude(),
                dto.sourceEventId(),
                dto.recordedAt()
        );
    }
}
