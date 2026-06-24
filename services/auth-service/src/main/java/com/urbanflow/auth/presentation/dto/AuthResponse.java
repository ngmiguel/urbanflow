package com.urbanflow.auth.presentation.dto;

import com.urbanflow.auth.application.dto.AuthTokensDto;
import com.urbanflow.common.enums.UserRole;

import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UUID userId,
        UserRole role
) {

    public static AuthResponse from(AuthTokensDto tokens) {
        return new AuthResponse(
                tokens.accessToken(),
                tokens.refreshToken(),
                "Bearer",
                tokens.accessTokenExpiresInSeconds(),
                tokens.userId(),
                tokens.role()
        );
    }
}
