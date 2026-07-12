package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Transaction;
import java.util.List;
import java.util.UUID;

public interface FetchTransactionsUseCase {
    List<Transaction> execute(UUID userId);
}
