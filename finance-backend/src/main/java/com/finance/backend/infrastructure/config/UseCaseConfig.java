package com.finance.backend.infrastructure.config;

import com.finance.backend.core.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            TokenServicePort tokenService) {

        // O Spring vai resolver os parâmetros automaticamente usando os Adapters que criamos
        return new AuthenticateUserUseCaseImpl(userRepository, passwordEncoder, tokenService);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(TokenServicePort tokenService) {
        return new RefreshTokenUseCaseImpl(tokenService);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        return new RegisterUserUseCaseImpl(userRepository, passwordEncoder);
    }
}
