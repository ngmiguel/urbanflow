package com.urbanflow.event.presentation.dto;

import com.urbanflow.event.domain.model.UrbanEventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ScheduleUrbanEventRequest(
        @NotNull UrbanEventType type,
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String zoneId,
        double latitude,
        double longitude,
        @NotNull Instant startsAt,
        @NotNull Instant endsAt,
        @Min(1) int expectedAttendance,
        String organizer
) {
}
