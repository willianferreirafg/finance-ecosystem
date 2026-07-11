package com.finance.backend.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String name,

        @NotBlank(message = "O ícone/emoji é obrigatório")
        String icon
) {}
