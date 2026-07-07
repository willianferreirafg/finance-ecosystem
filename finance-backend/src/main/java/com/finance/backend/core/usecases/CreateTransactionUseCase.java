package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface CreateTransactionUseCase {
    Transaction execute(UUID userId, UUID categoryId, String description, BigDecimal amount, LocalDate date, TransactionType type, boolean paid);
}
