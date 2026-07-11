package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    Optional<UUID> findIdByEmail(String email);
    User save(User user);
}
