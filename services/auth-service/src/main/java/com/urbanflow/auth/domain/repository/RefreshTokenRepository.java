package com.urbanflow.auth.domain.repository;

import com.urbanflow.auth.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void revokeAllByUserId(java.util.UUID userId);
}
