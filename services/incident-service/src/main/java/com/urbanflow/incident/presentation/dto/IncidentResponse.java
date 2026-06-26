package com.urbanflow.incident.presentation.dto;

import com.urbanflow.incident.application.dto.IncidentDto;
import com.urbanflow.incident.domain.model.IncidentSeverity;
import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.domain.model.IncidentType;

import java.time.Instant;
import java.util.UUID;

public record IncidentResponse(
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

    public static IncidentResponse from(IncidentDto dto) {
        return new IncidentResponse(
                dto.id(),
                dto.type(),
                dto.severity(),
                dto.status(),
                dto.description(),
                dto.zoneId(),
                dto.latitude(),
                dto.longitude(),
                dto.reportedBy(),
                dto.resolvedAt(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
