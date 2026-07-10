package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Transaction;
import java.util.List;
import java.util.Map;

public interface FinancialReportPort {
    byte[] generatePdfReport(List<Transaction> transactions, Map<String, Object> parameters);
}
