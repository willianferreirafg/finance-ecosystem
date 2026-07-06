package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.core.domain.model.Category;
import com.finance.backend.core.domain.model.User;
import com.finance.backend.core.usecases.CategoryRepositoryPort;
import com.finance.backend.infrastructure.database.entities.CategoryEntity;
import com.finance.backend.infrastructure.database.entities.UserEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final SpringDataCategoryRepository repository;

    public CategoryRepositoryAdapter(SpringDataCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Category> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Category> categories) {
        List<CategoryEntity> entities = categories.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        repository.saveAll(entities);
    }

    private CategoryEntity toEntity(Category domain) {
        if (domain == null) return null;

        UserEntity userEntity = UserEntity.builder()
                .id(domain.getUser().getId())
                .name(domain.getUser().getName())
                .email(domain.getUser().getEmail())
                .password(domain.getUser().getPassword())
                .createdAt(domain.getUser().getCreatedAt())
                .build();

        return CategoryEntity.builder()
                .id(domain.getId() != null ? domain.getId() : null)
                .name(domain.getName())
                .icon(domain.getIcon())
                .color(domain.getColor())
                .user(userEntity)
                .build();
    }

    private Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;

        User user = new User(
                entity.getUser().getId(),
                entity.getUser().getName(),
                entity.getUser().getEmail(),
                entity.getUser().getPassword(),
                entity.getUser().getCreatedAt()
        );

        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .color(entity.getColor())
                .user(user)
                .build();
    }
}
