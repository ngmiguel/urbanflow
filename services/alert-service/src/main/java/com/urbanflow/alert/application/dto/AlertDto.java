package com.urbanflow.alert.application.dto;

import com.urbanflow.alert.domain.model.AlertSeverity;
import com.urbanflow.alert.domain.model.AlertStatus;

import java.time.Instant;
import java.util.UUID;

public record AlertDto(
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
}
