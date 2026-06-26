package com.urbanflow.alert.presentation.dto;

import com.urbanflow.alert.application.dto.AlertRuleDto;
import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.alert.domain.model.RuleSourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAlertRuleRequest(
        @NotBlank @Size(max = 255) String name,
        @NotNull RuleSourceType sourceType,
        @Size(max = 100) String zoneId,
        @NotNull AlertSeverity outputSeverity,
        AlertSeverity incidentMinSeverity,
        CongestionLevel minCongestionLevel,
        @Size(max = 100) String sensorType,
        Double sensorThreshold
) {
}
