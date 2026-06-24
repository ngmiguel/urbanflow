package com.urbanflow.auth.application.service;

import com.urbanflow.auth.application.dto.AuthTokensDto;
import com.urbanflow.auth.application.port.TokenProvider;
import com.urbanflow.auth.domain.model.User;
import com.urbanflow.auth.domain.repository.RefreshTokenRepository;
import com.urbanflow.auth.domain.repository.UserRepository;
import com.urbanflow.common.enums.UserRole;
import com.urbanflow.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthApplicationService authApplicationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                UUID.randomUUID(),
                "citizen@urbanflow.io",
                "hashed-password",
                "Miguel",
                "Nguema",
                UserRole.CITIZEN,
                true,
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        when(userRepository.findByEmail("citizen@urbanflow.io")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPasswordHash())).thenReturn(true);
        when(tokenProvider.generateAccessToken(user)).thenReturn("access-token");
        when(tokenProvider.generateRefreshTokenValue()).thenReturn("refresh-token");
        when(tokenProvider.hashRefreshToken("refresh-token")).thenReturn("refresh-hash");
        when(tokenProvider.getAccessTokenTtlSeconds()).thenReturn(900L);
        when(tokenProvider.getRefreshTokenTtlSeconds()).thenReturn(604800L);

        AuthTokensDto tokens = authApplicationService.login("citizen@urbanflow.io", "password123");

        assertEquals("access-token", tokens.accessToken());
        assertEquals("refresh-token", tokens.refreshToken());
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void shouldRejectInvalidCredentials() {
        when(userRepository.findByEmail("citizen@urbanflow.io")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(BusinessException.class, () ->
                authApplicationService.login("citizen@urbanflow.io", "wrong-password"));

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        when(userRepository.existsByEmail("citizen@urbanflow.io")).thenReturn(true);

        assertThrows(BusinessException.class, () ->
                authApplicationService.register(
                        "citizen@urbanflow.io",
                        "password123",
                        "Miguel",
                        "Nguema"
                ));
    }
}
