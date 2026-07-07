package com.finance.backend.infrastructure.entrypoints.dto;

import com.finance.backend.core.domain.model.Category;
import java.util.UUID;

public record CategoryResponseDto(
        UUID id,
        String name,
        String icon,
        String color
) {
    public static CategoryResponseDto fromDomain(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getIcon(),
                category.getColor()
        );
    }
}
