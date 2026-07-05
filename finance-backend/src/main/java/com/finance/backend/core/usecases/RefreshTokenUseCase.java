package com.finance.backend.core.usecases;

public interface RefreshTokenUseCase {
    AuthenticateUserUseCase.AuthenticationResult execute(String refreshToken);
}
