package com.finance.backend.infrastructure.database.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import java.io.Serializable;
import java.time.Instant;

@RedisHash(value = "refresh_tokens", timeToLive = 604800) // TTL de 7 dias automaticamente gerenciado pelo Redis
public record RefreshTokenEntity(
        @Id
        String token,
        @Indexed
        String userEmail,
        Instant expiryDate,
        boolean revoked
) implements Serializable {}
