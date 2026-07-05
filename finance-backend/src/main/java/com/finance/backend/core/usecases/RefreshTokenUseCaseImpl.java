package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.exceptions.InvalidTokenException;

import java.time.Instant;
import java.util.UUID;

public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final TokenServicePort tokenService;

    public RefreshTokenUseCaseImpl(TokenServicePort tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public AuthenticateUserUseCase.AuthenticationResult execute(String refreshToken) {
        String email = tokenService.validateAndRotateRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Refresh Token inválido, expirado ou já utilizado."));

        // Gera o novo par de chaves (Rotação Completa)
        String newAccessToken = tokenService.generateAccessToken(email);
        String newRefreshToken = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(604800); // +7 dias

        tokenService.saveRefreshToken(newRefreshToken, email, expiresAt);

        return new AuthenticateUserUseCase.AuthenticationResult(newAccessToken, newRefreshToken, expiresAt);
    }
}
