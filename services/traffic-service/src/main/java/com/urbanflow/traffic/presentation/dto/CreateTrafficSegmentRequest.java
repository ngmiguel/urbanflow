package com.urbanflow.traffic.presentation.dto;

import com.urbanflow.common.enums.CongestionLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTrafficSegmentRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 100) String zoneId,
        @NotNull CongestionLevel congestionLevel,
        @Min(0) @Max(200) double averageSpeedKmh,
        @Min(0) int vehicleCount,
        @Min(-90) @Max(90) double latitude,
        @Min(-180) @Max(180) double longitude
) {
}
