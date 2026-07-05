package com.finance.backend.infrastructure.providers;

import com.finance.backend.core.usecases.TokenServicePort;
import com.finance.backend.infrastructure.database.entities.RefreshTokenEntity;
import com.finance.backend.infrastructure.database.repositories.RefreshTokenRedisRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class TokenServiceAdapter implements TokenServicePort {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository redisRepository;

    public TokenServiceAdapter(JwtTokenProvider jwtTokenProvider, RefreshTokenRedisRepository redisRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisRepository = redisRepository;
    }

    @Override
    public String generateAccessToken(String email) {
        return jwtTokenProvider.generateAccessToken(email);
    }

    @Override
    public void saveRefreshToken(String token, String email, Instant expiresAt) {
        // Deleta tokens antigos do usuário para manter o Redis limpo (1 sessão por usuário neste modelo)
        try {
            redisRepository.deleteByUserEmail(email);
        } catch (Exception e) {
            // Log silencioso se não houver token anterior
        }

        var entity = new RefreshTokenEntity(token, email, expiresAt, false);
        redisRepository.save(entity);
    }
}