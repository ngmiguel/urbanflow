package com.urbanflow.simulator.presentation.dto;

import jakarta.validation.constraints.Min;

public record StartScenarioRequest(
        String zoneId,
        @Min(10) Integer durationSeconds
) {
}
