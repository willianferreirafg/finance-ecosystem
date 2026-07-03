package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    User save(User user);
}
