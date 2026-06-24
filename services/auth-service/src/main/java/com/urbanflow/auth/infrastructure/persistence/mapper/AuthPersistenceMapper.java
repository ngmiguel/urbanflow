package com.urbanflow.auth.infrastructure.persistence.mapper;

import com.urbanflow.auth.domain.model.RefreshToken;
import com.urbanflow.auth.domain.model.User;
import com.urbanflow.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.urbanflow.auth.infrastructure.persistence.entity.UserEntity;

public final class AuthPersistenceMapper {

    private AuthPersistenceMapper() {
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setRole(user.getRole());
        entity.setEnabled(user.isEnabled());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(refreshToken.getId());
        entity.setUserId(refreshToken.getUserId());
        entity.setTokenHash(refreshToken.getTokenHash());
        entity.setExpiresAt(refreshToken.getExpiresAt());
        entity.setRevoked(refreshToken.isRevoked());
        entity.setCreatedAt(refreshToken.getCreatedAt());
        return entity;
    }

    public static RefreshToken toDomain(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(),
                entity.getUserId(),
                entity.getTokenHash(),
                entity.getExpiresAt(),
                entity.isRevoked(),
                entity.getCreatedAt()
        );
    }
}
