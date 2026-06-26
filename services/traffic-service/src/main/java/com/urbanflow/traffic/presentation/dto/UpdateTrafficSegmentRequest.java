package com.urbanflow.traffic.presentation.dto;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.traffic.application.dto.TrafficSegmentDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record UpdateTrafficSegmentRequest(
        @NotNull CongestionLevel congestionLevel,
        @Min(0) @Max(200) double averageSpeedKmh,
        @Min(0) int vehicleCount
) {
}
