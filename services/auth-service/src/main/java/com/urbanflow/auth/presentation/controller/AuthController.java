package com.urbanflow.auth.presentation.controller;

import com.urbanflow.auth.application.service.AuthApplicationService;
import com.urbanflow.auth.presentation.dto.AuthResponse;
import com.urbanflow.auth.presentation.dto.LoginRequest;
import com.urbanflow.auth.presentation.dto.RefreshTokenRequest;
import com.urbanflow.auth.presentation.dto.RegisterRequest;
import com.urbanflow.auth.presentation.dto.UserResponse;
import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/auth")
@Tag(name = "Authentication", description = "User registration, login and token management")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new citizen account")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok(
                UserResponse.from(authApplicationService.register(
                        request.email(),
                        request.password(),
                        request.firstName(),
                        request.lastName()
                )),
                "User registered successfully"
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and obtain JWT tokens")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(
                AuthResponse.from(authApplicationService.login(request.email(), request.password()))
        );
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using a valid refresh token")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.ok(
                AuthResponse.from(authApplicationService.refresh(request.refreshToken()))
        );
    }

    @GetMapping("/me")
    @Operation(summary = "Get the authenticated user profile")
    public ApiResponse<UserResponse> me(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ApiResponse.ok(UserResponse.from(authApplicationService.getProfile(userId)));
    }
}
