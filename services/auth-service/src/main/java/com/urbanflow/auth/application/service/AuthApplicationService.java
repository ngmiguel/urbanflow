package com.urbanflow.auth.application.service;

import com.urbanflow.auth.application.dto.AuthTokensDto;
import com.urbanflow.auth.application.dto.UserProfileDto;
import com.urbanflow.auth.application.port.TokenProvider;
import com.urbanflow.auth.domain.model.RefreshToken;
import com.urbanflow.auth.domain.model.User;
import com.urbanflow.auth.domain.repository.RefreshTokenRepository;
import com.urbanflow.auth.domain.repository.UserRepository;
import com.urbanflow.common.enums.UserRole;
import com.urbanflow.common.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthApplicationService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            TokenProvider tokenProvider,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserProfileDto register(
            String email,
            String password,
            String firstName,
            String lastName
    ) {
        if (userRepository.existsByEmail(email.toLowerCase())) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email is already registered");
        }

        User user = User.createNew(
                email,
                passwordEncoder.encode(password),
                firstName,
                lastName,
                UserRole.CITIZEN
        );

        User saved = userRepository.save(user);
        return toProfile(saved);
    }

    @Transactional
    public AuthTokensDto login(String email, String password) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new BusinessException("INVALID_CREDENTIALS", "Invalid email or password"));

        if (!user.isEnabled() || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException("INVALID_CREDENTIALS", "Invalid email or password");
        }

        return issueTokens(user);
    }

    @Transactional
    public AuthTokensDto refresh(String rawRefreshToken) {
        String tokenHash = tokenProvider.hashRefreshToken(rawRefreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new BusinessException("INVALID_REFRESH_TOKEN", "Refresh token is invalid"));

        if (!storedToken.isActive()) {
            throw new BusinessException("INVALID_REFRESH_TOKEN", "Refresh token is expired or revoked");
        }

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new BusinessException("INVALID_REFRESH_TOKEN", "Refresh token is invalid"));

        storedToken.revoke();
        refreshTokenRepository.save(storedToken);

        return issueTokens(user);
    }

    @Transactional(readOnly = true)
    public UserProfileDto getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));
        return toProfile(user);
    }

    private AuthTokensDto issueTokens(User user) {
        String accessToken = tokenProvider.generateAccessToken(user);
        String rawRefreshToken = tokenProvider.generateRefreshTokenValue();
        RefreshToken refreshToken = RefreshToken.create(
                user.getId(),
                tokenProvider.hashRefreshToken(rawRefreshToken),
                Instant.now().plusSeconds(tokenProvider.getRefreshTokenTtlSeconds())
        );
        refreshTokenRepository.save(refreshToken);

        return new AuthTokensDto(
                accessToken,
                rawRefreshToken,
                tokenProvider.getAccessTokenTtlSeconds(),
                user.getId(),
                user.getRole()
        );
    }

    private UserProfileDto toProfile(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
