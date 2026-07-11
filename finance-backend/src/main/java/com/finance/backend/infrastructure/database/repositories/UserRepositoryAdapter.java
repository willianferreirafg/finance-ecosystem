package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.core.domain.model.User;
import com.finance.backend.core.usecases.UserRepositoryPort;
import com.finance.backend.infrastructure.database.entities.UserEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<UUID> findIdByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserEntity::getId); // Extrai apenas o UUID da entidade encontrada
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> new User(
                        entity.getId(),
                        entity.getName(),
                        entity.getEmail(),
                        entity.getPassword(),
                        entity.getCreatedAt()
                ));
    }

    @Override
    public User save(User user) {
        // se o ID for nulo ou se o objetivo for sempre salvar um novo no cadastro,
        // deixar o JPA gerar o UUID nativamente.
        var entity = UserEntity.builder()
                // se o ID vier preenchido do domínio (ex: atualização), repassa.
                // se for nulo ou um cadastro novo, deixa nulo para o Hibernate dar INSERT.
                .id(user.getId() != null ? user.getId() : null)
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        var saved = repository.save(entity);

        // retorna o modelo do Core preenchido com o ID e data reais gerados pelo banco
        return new User(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getCreatedAt()
        );
    }
}
