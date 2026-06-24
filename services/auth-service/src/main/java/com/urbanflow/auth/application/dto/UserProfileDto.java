package com.urbanflow.auth.application.dto;

import com.urbanflow.common.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        Instant createdAt
) {
}
