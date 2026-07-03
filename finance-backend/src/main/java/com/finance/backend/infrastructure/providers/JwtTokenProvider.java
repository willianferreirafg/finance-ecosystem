package com.finance.backend.infrastructure.providers;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Gera uma chave segura adequada para o algoritmo HMAC-SHA padrão
    private final SecretKey key = Jwts.SIG.HS256.key().build();
    private final long jwtExpirationInMs = 900000; // 15 Minutos

    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key) // Assina diretamente com a SecretKey estável
                .compact();
    }

    public String getEmailFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(key) // Substitui o antigo setSigningKey
                .build()
                .parseSignedClaims(token) // Substitui o parseClaimsJws
                .getPayload() // Substitui o getBody
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (Exception ex) {
            // Em produção sênior, capturaríamos exceções específicas (ExpiredJwtException, MalformedJwtException)
            return false;
        }
    }
}
