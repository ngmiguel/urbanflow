package com.urbanflow.incident.presentation.dto;

import com.urbanflow.incident.domain.model.IncidentSeverity;
import com.urbanflow.incident.domain.model.IncidentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateIncidentRequest(
        @NotNull IncidentSeverity severity,
        @NotBlank @Size(max = 1000) String description,
        IncidentStatus status
) {
}
