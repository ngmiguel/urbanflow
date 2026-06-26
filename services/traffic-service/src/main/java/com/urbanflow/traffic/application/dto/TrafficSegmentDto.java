package com.urbanflow.traffic.application.dto;

import com.urbanflow.common.enums.CongestionLevel;

import java.time.Instant;
import java.util.UUID;

public record TrafficSegmentDto(
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
}
