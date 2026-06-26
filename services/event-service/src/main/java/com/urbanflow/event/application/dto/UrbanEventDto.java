package com.urbanflow.event.application.dto;

import com.urbanflow.event.domain.model.UrbanEvent;
import com.urbanflow.event.domain.model.UrbanEventStatus;
import com.urbanflow.event.domain.model.UrbanEventType;

import java.time.Instant;
import java.util.UUID;

public record UrbanEventDto(
        UUID id,
        UrbanEventType type,
        UrbanEventStatus status,
        String title,
        String description,
        String zoneId,
        double latitude,
        double longitude,
        Instant startsAt,
        Instant endsAt,
        int expectedAttendance,
        String organizer,
        Instant createdAt,
        Instant updatedAt
) {

    public static UrbanEventDto from(UrbanEvent event) {
        return new UrbanEventDto(
                event.getId(),
                event.getType(),
                event.getStatus(),
                event.getTitle(),
                event.getDescription(),
                event.getZoneId(),
                event.getLatitude(),
                event.getLongitude(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.getExpectedAttendance(),
                event.getOrganizer(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}
