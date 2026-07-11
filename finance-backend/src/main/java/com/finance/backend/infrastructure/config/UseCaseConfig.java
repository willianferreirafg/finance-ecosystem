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

        // O Spring vai resolver os parâmetros automaticamente usando os Adapters que estão criados
        return new AuthenticateUserUseCaseImpl(userRepository, passwordEncoder, tokenService);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(TokenServicePort tokenService) {
        return new RefreshTokenUseCaseImpl(tokenService);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepository,
                                                   PasswordEncoderPort passwordEncoder,
                                                   CategoryRepositoryPort categoryRepository) {
        return new RegisterUserUseCaseImpl(userRepository, passwordEncoder, categoryRepository);
    }

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionRepositoryPort transactionRepository,
                                                             UserRepositoryPort userRepository,
                                                             CategoryRepositoryPort categoryRepository) {
        return new CreateTransactionUseCaseImpl(transactionRepository, userRepository, categoryRepository);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(CategoryRepositoryPort categoryRepository,
                                                       UserRepositoryPort userRepository) {
        return new CreateCategoryUseCaseImpl(categoryRepository, userRepository);
    }
}
