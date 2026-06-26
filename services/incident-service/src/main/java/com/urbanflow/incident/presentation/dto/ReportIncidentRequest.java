package com.urbanflow.incident.presentation.dto;

import com.urbanflow.incident.application.dto.IncidentDto;
import com.urbanflow.incident.domain.model.IncidentSeverity;
import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.domain.model.IncidentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportIncidentRequest(
        @NotNull IncidentType type,
        @NotNull IncidentSeverity severity,
        @NotBlank @Size(max = 1000) String description,
        @NotBlank @Size(max = 100) String zoneId,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @Size(max = 255) String reportedBy
) {
}
