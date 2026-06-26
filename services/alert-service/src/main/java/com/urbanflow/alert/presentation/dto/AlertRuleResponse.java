package com.urbanflow.alert.presentation.dto;

import com.urbanflow.alert.application.dto.AlertRuleDto;
import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.alert.domain.model.RuleSourceType;

import java.time.Instant;
import java.util.UUID;

public record AlertRuleResponse(
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

    public static AlertRuleResponse from(AlertRuleDto dto) {
        return new AlertRuleResponse(
                dto.id(),
                dto.name(),
                dto.sourceType(),
                dto.zoneId(),
                dto.outputSeverity(),
                dto.enabled(),
                dto.incidentMinSeverity(),
                dto.minCongestionLevel(),
                dto.sensorType(),
                dto.sensorThreshold(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
