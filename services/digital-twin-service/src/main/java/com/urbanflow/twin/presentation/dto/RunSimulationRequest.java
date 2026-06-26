package com.urbanflow.twin.presentation.dto;

import com.urbanflow.twin.domain.model.WhatIfScenarioType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RunSimulationRequest(
        @NotBlank String zoneId,
        @NotNull WhatIfScenarioType scenarioType,
        String incidentSeverity,
        Integer closureDurationMinutes
) {
}
