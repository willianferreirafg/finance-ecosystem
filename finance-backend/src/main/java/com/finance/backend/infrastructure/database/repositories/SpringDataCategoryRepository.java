package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.infrastructure.database.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    List<CategoryEntity> findByUserId(UUID userId);
}
