package com.urbanflow.auth.infrastructure.persistence;

import com.urbanflow.auth.domain.model.RefreshToken;
import com.urbanflow.auth.domain.repository.RefreshTokenRepository;
import com.urbanflow.auth.infrastructure.persistence.mapper.AuthPersistenceMapper;
import com.urbanflow.auth.infrastructure.persistence.repository.RefreshTokenJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository refreshTokenJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return AuthPersistenceMapper.toDomain(
                refreshTokenJpaRepository.save(AuthPersistenceMapper.toEntity(refreshToken))
        );
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash).map(AuthPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UUID userId) {
        refreshTokenJpaRepository.revokeAllByUserId(userId);
    }
}
