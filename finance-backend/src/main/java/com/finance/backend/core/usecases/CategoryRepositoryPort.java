package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryPort {
    Category save(Category category);
    Optional<Category> findById(UUID id);
    List<Category> findByUserId(UUID userId);
    void saveAll(List<Category> categories);
}
