package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.domain.model.Category;
import com.finance.backend.core.usecases.CategoryRepositoryPort;
import com.finance.backend.core.usecases.CreateCategoryUseCase;
import com.finance.backend.infrastructure.entrypoints.dto.CategoryRequestDto;
import com.finance.backend.infrastructure.entrypoints.dto.CategoryResponseDto;
import com.finance.backend.core.usecases.UserRepositoryPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepositoryPort categoryRepository;
    private final UserRepositoryPort userRepository;
    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(CategoryRepositoryPort categoryRepository, UserRepositoryPort userRepository, CreateCategoryUseCase createCategoryUseCase) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.createCategoryUseCase = createCategoryUseCase;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> listMyCategories(@AuthenticationPrincipal Object principal) {

        String email = principal.toString();

        // Buscamos o UUID no banco de dados usando o e-mail
        UUID userId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        List<CategoryResponseDto> categories = categoryRepository.findByUserId(userId).stream()
                .map(CategoryResponseDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(
            @Valid @RequestBody CategoryRequestDto request,
            @AuthenticationPrincipal Object principal) {

        String email = principal.toString();

        // Obtém o UUID do usuário através do e-mail vindo do Token
        UUID userId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // Executa a criação através da camada de Core/Domínio
        Category createdCategory = createCategoryUseCase.execute(
                userId,
                request.name(),
                request.icon()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CategoryResponseDto.fromDomain(createdCategory));
    }
}
