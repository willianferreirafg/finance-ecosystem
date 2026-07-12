package com.finance.backend.core.usecases;

import com.finance.backend.infrastructure.entrypoints.dto.TransactionSummaryDto;
import java.util.UUID;

public interface GetTransactionSummaryUseCase {
    TransactionSummaryDto execute(UUID userId);
}
