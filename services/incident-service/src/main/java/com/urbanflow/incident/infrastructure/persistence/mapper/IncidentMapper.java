package com.urbanflow.incident.infrastructure.persistence.mapper;

import com.urbanflow.incident.domain.model.Incident;
import com.urbanflow.incident.infrastructure.persistence.entity.IncidentEntity;

public final class IncidentMapper {

    private IncidentMapper() {
    }

    public static Incident toDomain(IncidentEntity entity) {
        return new Incident(
                entity.getId(),
                entity.getType(),
                entity.getSeverity(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getZoneId(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getReportedBy(),
                entity.getResolvedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static IncidentEntity toEntity(Incident incident) {
        IncidentEntity entity = new IncidentEntity();
        entity.setId(incident.getId());
        entity.setType(incident.getType());
        entity.setSeverity(incident.getSeverity());
        entity.setStatus(incident.getStatus());
        entity.setDescription(incident.getDescription());
        entity.setZoneId(incident.getZoneId());
        entity.setLatitude(incident.getLatitude());
        entity.setLongitude(incident.getLongitude());
        entity.setReportedBy(incident.getReportedBy());
        entity.setResolvedAt(incident.getResolvedAt());
        entity.setCreatedAt(incident.getCreatedAt());
        entity.setUpdatedAt(incident.getUpdatedAt());
        return entity;
    }
}
