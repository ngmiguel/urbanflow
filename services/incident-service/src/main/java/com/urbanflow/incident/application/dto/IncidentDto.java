package com.urbanflow.incident.application.dto;

import com.urbanflow.incident.domain.model.IncidentSeverity;
import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.domain.model.IncidentType;

import java.time.Instant;
import java.util.UUID;

public record IncidentDto(
        UUID id,
        IncidentType type,
        IncidentSeverity severity,
        IncidentStatus status,
        String description,
        String zoneId,
        double latitude,
        double longitude,
        String reportedBy,
        Instant resolvedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
