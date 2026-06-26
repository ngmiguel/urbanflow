package com.urbanflow.iot.presentation.dto;

import com.urbanflow.iot.application.dto.IoTDeviceDto;
import com.urbanflow.iot.domain.model.DeviceStatus;
import com.urbanflow.iot.domain.model.SensorType;

import java.time.Instant;

public record DeviceResponse(
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

    public static DeviceResponse from(IoTDeviceDto dto) {
        return new DeviceResponse(
                dto.deviceId(),
                dto.name(),
                dto.sensorType(),
                dto.zoneId(),
                dto.latitude(),
                dto.longitude(),
                dto.status(),
                dto.lastHeartbeatAt(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
