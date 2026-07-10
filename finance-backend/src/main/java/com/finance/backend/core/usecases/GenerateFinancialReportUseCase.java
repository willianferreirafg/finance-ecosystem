package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.usecases.FinancialReportPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GenerateFinancialReportUseCase {

    private final TransactionRepositoryPort transactionRepository; // Assumindo que você tenha essa porta de transações
    private final FinancialReportPort financialReportPort;

    public GenerateFinancialReportUseCase(TransactionRepositoryPort transactionRepository, FinancialReportPort financialReportPort) {
        this.transactionRepository = transactionRepository;
        this.financialReportPort = financialReportPort;
    }

    public byte[] execute(UUID userId, LocalDate startDate, LocalDate endDate) {
        // 1. Busca os dados reais de transações do usuário no período escolhido
        List<Transaction> transactions = transactionRepository.findByUserIdAndPeriod(userId, startDate, endDate);

        // 2. Prepara os parâmetros do cabeçalho do Jasper
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "Relatório Financeiro Consolidado");
        parameters.put("START_DATE", startDate.toString());
        parameters.put("END_DATE", endDate.toString());
        parameters.put("TOTAL_COUNT", transactions.size());

        // 3. Delega a geração do binário PDF para o adaptador do Jasper
        return financialReportPort.generatePdfReport(transactions, parameters);
    }
}
