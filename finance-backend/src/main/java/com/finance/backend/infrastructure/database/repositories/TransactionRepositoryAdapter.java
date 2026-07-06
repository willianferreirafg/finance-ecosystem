package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.core.domain.model.Category;
import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.domain.model.User;
import com.finance.backend.core.usecases.TransactionRepositoryPort;
import com.finance.backend.infrastructure.database.entities.CategoryEntity;
import com.finance.backend.infrastructure.database.entities.TransactionEntity;
import com.finance.backend.infrastructure.database.entities.UserEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final SpringDataTransactionRepository repository;

    public TransactionRepositoryAdapter(SpringDataTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = toEntity(transaction);
        TransactionEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    private TransactionEntity toEntity(Transaction domain) {
        if (domain == null) return null;

        UserEntity userEntity = UserEntity.builder()
                .id(domain.getUser().getId())
                .name(domain.getUser().getName())
                .email(domain.getUser().getEmail())
                .password(domain.getUser().getPassword())
                .createdAt(domain.getUser().getCreatedAt())
                .build();

        CategoryEntity categoryEntity = CategoryEntity.builder()
                .id(domain.getCategory().getId())
                .name(domain.getCategory().getName())
                .icon(domain.getCategory().getIcon())
                .color(domain.getCategory().getColor())
                .user(userEntity)
                .build();

        return TransactionEntity.builder()
                .id(domain.getId() != null ? domain.getId() : null)
                .description(domain.getDescription())
                .amount(domain.getAmount())
                .date(domain.getDate())
                .type(domain.getType())
                .paid(domain.isPaid())
                .user(userEntity)
                .category(categoryEntity)
                .build();
    }

    private Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;

        User user = new User(
                entity.getUser().getId(),
                entity.getUser().getName(),
                entity.getUser().getEmail(),
                entity.getUser().getPassword(),
                entity.getUser().getCreatedAt()
        );

        Category category = Category.builder()
                .id(entity.getCategory().getId())
                .name(entity.getCategory().getName())
                .icon(entity.getCategory().getIcon())
                .color(entity.getCategory().getColor())
                .user(user)
                .build();

        return Transaction.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .type(entity.getType())
                .paid(entity.isPaid())
                .user(user)
                .category(category)
                .build();
    }
}
