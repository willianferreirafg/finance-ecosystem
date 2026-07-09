package com.finance.backend.infrastructure.adapters;

import com.finance.backend.core.domain.model.Category;
import com.finance.backend.core.domain.model.User;
import com.finance.backend.core.usecases.CategoryRepositoryPort;
import com.finance.backend.infrastructure.database.entities.CategoryEntity;
import com.finance.backend.infrastructure.database.entities.UserEntity;
import com.finance.backend.infrastructure.database.repositories.SpringDataCategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final SpringDataCategoryRepository repository;

    public CategoryPersistenceAdapter(SpringDataCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "categories", key = "#userId")
    public List<Category> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(entity -> Category.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .icon(entity.getIcon())
                        .color(entity.getColor())
                        // Criando o User de domínio usando o construtor puro e passando apenas o ID
                        .user(new User(entity.getUser().getId(), null, null, null, null))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "categories", key = "#category.getUser().getId()")
    public Category save(Category category) {
        UserEntity userRef = new UserEntity();
        userRef.setId(category.getUser().getId());

        CategoryEntity entity = CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .color(category.getColor())
                .user(userRef)
                .build();

        CategoryEntity saved = repository.save(entity);

        return Category.builder()
                .id(saved.getId())
                .name(saved.getName())
                .icon(saved.getIcon())
                .color(saved.getColor())
                // Criando o User de domínio usando o construtor puro com o ID salvo
                .user(new User(saved.getUser().getId(), null, null, null, null))
                .build();
    }

    @Override
    public java.util.Optional<Category> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> Category.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .icon(entity.getIcon())
                        .color(entity.getColor())
                        // Criando o User de domínio usando o construtor puro com o ID
                        .user(new User(entity.getUser().getId(), null, null, null, null))
                        .build());
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public void saveAll(List<Category> categories) {
        List<CategoryEntity> entities = categories.stream()
                .map(category -> {
                    UserEntity userRef = new UserEntity();
                    userRef.setId(category.getUser().getId());

                    return CategoryEntity.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .icon(category.getIcon())
                            .color(category.getColor())
                            .user(userRef)
                            .build();
                })
                .collect(Collectors.toList());

        repository.saveAll(entities);
    }
}
