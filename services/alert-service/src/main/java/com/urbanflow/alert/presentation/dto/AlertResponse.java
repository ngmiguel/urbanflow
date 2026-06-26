package com.urbanflow.alert.presentation.dto;

import com.urbanflow.alert.application.dto.AlertDto;
import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.AlertStatus;

import java.time.Instant;
import java.util.UUID;

public record AlertResponse(
        UUID id,
        UUID ruleId,
        String alertType,
        AlertSeverity severity,
        String message,
        String zoneId,
        AlertStatus status,
        String sourceEventId,
        Instant createdAt,
        Instant updatedAt
) {

    public static AlertResponse from(AlertDto dto) {
        return new AlertResponse(
                dto.id(),
                dto.ruleId(),
                dto.alertType(),
                dto.severity(),
                dto.message(),
                dto.zoneId(),
                dto.status(),
                dto.sourceEventId(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
