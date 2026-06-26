package com.urbanflow.iot.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TelemetryRequest(
        @Min(0) double value,
        @NotBlank @Size(max = 20) String unit
) {
}
