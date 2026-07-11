package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Category;
import java.util.UUID;

public interface CreateCategoryUseCase {
    Category execute(UUID userId, String name, String icon);
}