package com.urbanflow.event.infrastructure.persistence.mapper;

import com.urbanflow.event.domain.model.UrbanEvent;
import com.urbanflow.event.infrastructure.persistence.entity.UrbanEventEntity;

public final class UrbanEventMapper {

    private UrbanEventMapper() {
    }

    public static UrbanEventEntity toEntity(UrbanEvent event) {
        UrbanEventEntity entity = new UrbanEventEntity();
        entity.setId(event.getId());
        entity.setType(event.getType());
        entity.setStatus(event.getStatus());
        entity.setTitle(event.getTitle());
        entity.setDescription(event.getDescription());
        entity.setZoneId(event.getZoneId());
        entity.setLatitude(event.getLatitude());
        entity.setLongitude(event.getLongitude());
        entity.setStartsAt(event.getStartsAt());
        entity.setEndsAt(event.getEndsAt());
        entity.setExpectedAttendance(event.getExpectedAttendance());
        entity.setOrganizer(event.getOrganizer());
        entity.setCreatedAt(event.getCreatedAt());
        entity.setUpdatedAt(event.getUpdatedAt());
        return entity;
    }

    public static UrbanEvent toDomain(UrbanEventEntity entity) {
        return new UrbanEvent(
                entity.getId(),
                entity.getType(),
                entity.getStatus(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getZoneId(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getExpectedAttendance(),
                entity.getOrganizer(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
