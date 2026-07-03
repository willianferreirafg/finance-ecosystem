package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.core.domain.model.User;
import com.finance.backend.core.usecases.UserRepositoryPort;
import com.finance.backend.infrastructure.database.entities.UserEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword(), entity.getCreatedAt()));
    }

    @Override
    public User save(User user) {
        var entity = UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        var saved = repository.save(entity);
        return new User(saved.getId(), saved.getName(), saved.getEmail(), saved.getPassword(), saved.getCreatedAt());
    }
}
