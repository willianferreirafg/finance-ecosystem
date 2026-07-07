package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.usecases.CategoryRepositoryPort;
import com.finance.backend.infrastructure.entrypoints.dto.CategoryResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepositoryPort categoryRepository;

    public CategoryController(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> listMyCategories(@AuthenticationPrincipal Object principal) {

        UUID userId = UUID.fromString(principal.toString());

        List<CategoryResponseDto> categories = categoryRepository.findByUserId(userId).stream()
                .map(CategoryResponseDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categories);
    }
}
