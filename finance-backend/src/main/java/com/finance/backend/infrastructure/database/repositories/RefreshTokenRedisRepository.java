package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.infrastructure.database.entities.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUserEmail(String userEmail);
}
