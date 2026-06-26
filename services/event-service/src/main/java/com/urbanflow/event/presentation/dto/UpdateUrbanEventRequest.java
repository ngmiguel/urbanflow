package com.urbanflow.event.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UpdateUrbanEventRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Instant startsAt,
        @NotNull Instant endsAt,
        @Min(1) int expectedAttendance
) {
}
