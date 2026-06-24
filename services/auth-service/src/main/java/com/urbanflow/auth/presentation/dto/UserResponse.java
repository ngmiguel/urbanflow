package com.urbanflow.auth.presentation.dto;

import com.urbanflow.auth.application.dto.UserProfileDto;
import com.urbanflow.common.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        Instant createdAt
) {

    public static UserResponse from(UserProfileDto profile) {
        return new UserResponse(
                profile.id(),
                profile.email(),
                profile.firstName(),
                profile.lastName(),
                profile.role(),
                profile.createdAt()
        );
    }
}
