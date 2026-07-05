package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.User;
import java.time.Instant;
import java.util.UUID;

public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenServicePort tokenService;

    public AuthenticateUserUseCaseImpl(UserRepositoryPort userRepository,
                                       PasswordEncoderPort passwordEncoder,
                                       TokenServicePort tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public AuthenticationResult execute(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String accessToken = tokenService.generateAccessToken(user.getEmail());
        String refreshToken = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(604800); // 7 dias

        tokenService.saveRefreshToken(refreshToken, user.getEmail(), expiresAt);

        return new AuthenticationResult(accessToken, refreshToken, expiresAt);
    }
}
