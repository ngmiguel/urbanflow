package com.urbanflow.alert.presentation.dto;

import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.common.enums.CongestionLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAlertRuleRequest(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 100) String zoneId,
        @NotNull AlertSeverity outputSeverity,
        boolean enabled,
        AlertSeverity incidentMinSeverity,
        CongestionLevel minCongestionLevel,
        @Size(max = 100) String sensorType,
        Double sensorThreshold
) {
}
