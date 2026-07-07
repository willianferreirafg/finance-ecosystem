package com.finance.backend.infrastructure.entrypoints.dto;

import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate date,
        TransactionType type,
        boolean paid,
        UUID categoryId,
        String categoryName
) {
    public static TransactionResponseDto fromDomain(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getType(),
                transaction.isPaid(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName()
        );
    }
}
