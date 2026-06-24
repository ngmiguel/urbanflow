package com.urbanflow.auth.application.dto;

import com.urbanflow.common.enums.UserRole;

import java.util.UUID;

public record AuthTokensDto(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresInSeconds,
        UUID userId,
        UserRole role
) {
}
