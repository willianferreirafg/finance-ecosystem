package com.finance.backend.infrastructure.adapters;

import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.usecases.FinancialReportPort;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class JasperReportAdapter implements FinancialReportPort {

    @Override
    public byte[] generatePdfReport(List<Transaction> transactions, Map<String, Object> parameters) {
        try {
            // 1. Carrega o arquivo compilado ou o XML do template do Jasper
            InputStream reportStream = getClass().getResourceAsStream("/reports/financial_summary.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("Template do relatório não encontrado no caminho especificado.");
            }

            // 2. Compila o relatório em tempo de execução
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 3. Envolve a lista de transações do domínio em um DataSource compatível com o Jasper
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transactions);

            // 4. Preenche o relatório com os parâmetros e dados
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // 5. Exporta para um array de bytes (PDF)
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException e) {
            throw new RuntimeException("Erro ao gerar o relatório financeiro via JasperReports", e);
        }
    }
}
