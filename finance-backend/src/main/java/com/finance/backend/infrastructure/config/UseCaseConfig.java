package com.finance.backend.infrastructure.config;

import com.finance.backend.core.usecases.AuthenticateUserUseCase;
import com.finance.backend.core.usecases.AuthenticateUserUseCaseImpl;
import com.finance.backend.core.usecases.PasswordEncoderPort;
import com.finance.backend.core.usecases.TokenServicePort;
import com.finance.backend.core.usecases.UserRepositoryPort;
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
}
