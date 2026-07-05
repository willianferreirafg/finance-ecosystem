package com.finance.backend.core.usecases;

import java.time.Instant;

public interface TokenServicePort {
    String generateAccessToken(String email);
    void saveRefreshToken(String token, String email, Instant expiresAt);
}
