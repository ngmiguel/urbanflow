package com.urbanflow.iot.presentation.dto;

import com.urbanflow.iot.domain.model.SensorType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDeviceRequest(
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9-]+$") @Size(max = 100) String deviceId,
        @NotBlank @Size(max = 255) String name,
        @NotNull SensorType sensorType,
        @NotBlank @Size(max = 100) String zoneId,
        @Min(-90) @Max(90) double latitude,
        @Min(-180) @Max(180) double longitude
) {
}
