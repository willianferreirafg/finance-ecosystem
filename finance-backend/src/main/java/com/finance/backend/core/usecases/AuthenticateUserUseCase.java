package com.finance.backend.core.usecases;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public interface AuthenticateUserUseCase {
    AuthenticationResult execute(String email, String rawPassword);

    // record para envelopar o retorno (forma limpa e imutável)
    record AuthenticationResult(String accessToken, String refreshToken, Instant refreshTokenExpiresAt) {}
}
