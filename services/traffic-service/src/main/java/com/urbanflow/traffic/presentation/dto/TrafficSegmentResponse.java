package com.urbanflow.traffic.presentation.dto;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.traffic.application.dto.TrafficSegmentDto;

import java.time.Instant;
import java.util.UUID;

public record TrafficSegmentResponse(
        UUID id,
        String name,
        String zoneId,
        CongestionLevel congestionLevel,
        double averageSpeedKmh,
        int vehicleCount,
        double latitude,
        double longitude,
        Instant createdAt,
        Instant updatedAt
) {

    public static TrafficSegmentResponse from(TrafficSegmentDto dto) {
        return new TrafficSegmentResponse(
                dto.id(),
                dto.name(),
                dto.zoneId(),
                dto.congestionLevel(),
                dto.averageSpeedKmh(),
                dto.vehicleCount(),
                dto.latitude(),
                dto.longitude(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
