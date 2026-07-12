package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.exceptions.ResourceNotFoundException;
import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.domain.model.TransactionType;
import com.finance.backend.core.domain.model.User;
import com.finance.backend.core.usecases.FinancialReportPort;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GenerateFinancialReportUseCase {

    private final TransactionRepositoryPort transactionRepository; // Assumindo que você tenha essa porta de transações
    private final FinancialReportPort financialReportPort;
    private final UserRepositoryPort userRepository;

    public GenerateFinancialReportUseCase(TransactionRepositoryPort transactionRepository, FinancialReportPort financialReportPort, UserRepositoryPort userRepository) {
        this.transactionRepository = transactionRepository;
        this.financialReportPort = financialReportPort;
        this.userRepository = userRepository;
    }

    public byte[] execute(UUID userId, LocalDate start, LocalDate end) {
        // 1. Busca os dados do usuário para preencher o cabeçalho
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // 2. Busca a lista filtrada de transações do período
        List<Transaction> transactions = transactionRepository.findByUserIdAndPeriod(userId, start, end);

        // 3. Calcula os KPIs consolidados para os Cards do PDF
        BigDecimal sumIncomes = transactions.stream()
                .filter(t -> t.getType() == TransactionType.REVENUE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumExpenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = sumIncomes.subtract(sumExpenses);

        // 4. Injeta os parâmetros usando .put() (Correção do erro do Map)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userName", user.getName());
        parameters.put("userEmail", user.getEmail());
        parameters.put("periodStart", start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        parameters.put("periodEnd", end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        parameters.put("sumIncomes", sumIncomes);
        parameters.put("sumExpenses", sumExpenses);
        parameters.put("netBalance", netBalance);

        // 5. Delegação correta para o Adaptador de Infraestrutura via Porta (Correção do compiledReport)
        return financialReportPort.generatePdfReport(transactions, parameters);
    }
}
