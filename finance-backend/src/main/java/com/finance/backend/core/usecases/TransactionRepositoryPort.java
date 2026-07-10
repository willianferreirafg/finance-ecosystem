package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByUserIdAndPeriod(UUID userId, LocalDate startDate, LocalDate endDate);
}
