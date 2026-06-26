package com.urbanflow.alert.application.dto;

import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.AlertStatus;
import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.alert.domain.model.RuleSourceType;

import java.time.Instant;
import java.util.UUID;

public record AlertRuleDto(
        UUID id,
        String name,
        RuleSourceType sourceType,
        String zoneId,
        AlertSeverity outputSeverity,
        boolean enabled,
        AlertSeverity incidentMinSeverity,
        CongestionLevel minCongestionLevel,
        String sensorType,
        Double sensorThreshold,
        Instant createdAt,
        Instant updatedAt
) {
}
