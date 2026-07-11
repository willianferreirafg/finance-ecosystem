package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.usecases.CategoryRepositoryPort;
import com.finance.backend.infrastructure.entrypoints.dto.CategoryResponseDto;
import com.finance.backend.core.usecases.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepositoryPort categoryRepository;
    private final UserRepositoryPort userRepository;

    public CategoryController(CategoryRepositoryPort categoryRepository, UserRepositoryPort userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> listMyCategories(@AuthenticationPrincipal Object principal) {

        String email = principal.toString();

        // 2. Buscamos o UUID no banco de dados usando o e-mail
        UUID userId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        List<CategoryResponseDto> categories = categoryRepository.findByUserId(userId).stream()
                .map(CategoryResponseDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categories);
    }
}
