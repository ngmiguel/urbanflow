package com.urbanflow.auth.application.port;

import com.urbanflow.auth.domain.model.User;

public interface TokenProvider {

    String generateAccessToken(User user);

    long getAccessTokenTtlSeconds();

    String generateRefreshTokenValue();

    String hashRefreshToken(String rawToken);

    long getRefreshTokenTtlSeconds();
}
