package com.finance.backend.infrastructure.entrypoints.dto;

import com.finance.backend.core.domain.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequestDto(

        @NotNull(message = "O ID da categoria é obrigatório.")
        UUID categoryId,

        @NotBlank(message = "A descrição é obrigatória.")
        String description,

        @NotNull(message = "O valor é obrigatório.")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal amount,

        @NotNull(message = "A data é obrigatória.")
        LocalDate date,

        @NotNull(message = "O tipo da transação (REVENUE/EXPENSE) é obrigatório.")
        TransactionType type,

        boolean paid
) {}
