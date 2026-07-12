package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.domain.model.TransactionType;
import com.finance.backend.infrastructure.entrypoints.dto.TransactionSummaryDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class GetTransactionSummaryUseCaseImpl implements GetTransactionSummaryUseCase {

    private final TransactionRepositoryPort transactionRepository;

    public GetTransactionSummaryUseCaseImpl(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionSummaryDto execute(UUID userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        BigDecimal totalIncomes = transactions.stream()
                .filter(t -> t.getType() == TransactionType.REVENUE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncomes.subtract(totalExpenses);

        return new TransactionSummaryDto(totalIncomes, totalExpenses, balance);
    }
}
