package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Transaction;
import java.util.List;
import java.util.UUID;

public class FetchTransactionsUseCaseImpl implements FetchTransactionsUseCase {

    private final TransactionRepositoryPort transactionRepository;

    public FetchTransactionsUseCaseImpl(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> execute(UUID userId) {
        return transactionRepository.findByUserId(userId);
    }
}
