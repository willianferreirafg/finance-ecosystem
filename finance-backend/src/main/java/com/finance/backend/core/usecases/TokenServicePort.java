package com.finance.backend.core.usecases;

import java.time.Instant;
import java.util.Optional;

public interface TokenServicePort {
    String generateAccessToken(String email);
    void saveRefreshToken(String token, String email, Instant expiresAt);
    Optional<String> validateAndRotateRefreshToken(String refreshToken);
}
