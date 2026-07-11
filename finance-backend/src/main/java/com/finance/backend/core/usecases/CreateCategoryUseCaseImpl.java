package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Category;
import com.finance.backend.core.domain.model.User;
import com.finance.backend.core.domain.exceptions.ResourceNotFoundException;
import java.util.UUID;

public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final UserRepositoryPort userRepository;

    public CreateCategoryUseCaseImpl(CategoryRepositoryPort categoryRepository, UserRepositoryPort userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Category execute(UUID userId, String name, String icon) {
        // Busca o usuário completo para associar à categoria
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Category newCategory = Category.builder()
                .id(null) // Banco ou repositório gera o UUID
                .name(name)
                .icon(icon)
                .user(user)
                .build();

        return categoryRepository.save(newCategory);
    }
}
