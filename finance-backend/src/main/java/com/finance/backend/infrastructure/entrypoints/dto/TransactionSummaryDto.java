package com.finance.backend.infrastructure.entrypoints.dto;

import java.math.BigDecimal;

public record TransactionSummaryDto(
        BigDecimal totalIncomes,
        BigDecimal totalExpenses,
        BigDecimal balance
) {}
