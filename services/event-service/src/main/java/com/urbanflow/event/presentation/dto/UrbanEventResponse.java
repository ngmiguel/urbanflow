package com.urbanflow.event.presentation.dto;

import com.urbanflow.event.application.dto.UrbanEventDto;

public record UrbanEventResponse(
        String id,
        String type,
        String status,
        String title,
        String description,
        String zoneId,
        double latitude,
        double longitude,
        String startsAt,
        String endsAt,
        int expectedAttendance,
        String organizer,
        String createdAt,
        String updatedAt
) {

    public static UrbanEventResponse from(UrbanEventDto dto) {
        return new UrbanEventResponse(
                dto.id().toString(),
                dto.type().name(),
                dto.status().name(),
                dto.title(),
                dto.description(),
                dto.zoneId(),
                dto.latitude(),
                dto.longitude(),
                dto.startsAt().toString(),
                dto.endsAt().toString(),
                dto.expectedAttendance(),
                dto.organizer(),
                dto.createdAt().toString(),
                dto.updatedAt().toString()
        );
    }
}
