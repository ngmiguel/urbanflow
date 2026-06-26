package com.urbanflow.iot.application.dto;

import com.urbanflow.iot.domain.model.DeviceStatus;
import com.urbanflow.iot.domain.model.SensorType;

import java.time.Instant;

public record IoTDeviceDto(
        String deviceId,
        String name,
        SensorType sensorType,
        String zoneId,
        double latitude,
        double longitude,
        DeviceStatus status,
        Instant lastHeartbeatAt,
        Instant createdAt,
        Instant updatedAt
) {
}
