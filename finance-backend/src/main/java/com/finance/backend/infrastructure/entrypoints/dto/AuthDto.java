package com.finance.backend.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class AuthDto {
    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {}

    public record LoginResponse(
            String accessToken,
            String refreshToken,
            String tokenType,
            Instant expiresAt
    ) {}
}
