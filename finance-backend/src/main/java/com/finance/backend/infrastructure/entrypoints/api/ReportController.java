package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.usecases.GenerateFinancialReportUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final GenerateFinancialReportUseCase generateFinancialReportUseCase;

    public ReportController(GenerateFinancialReportUseCase generateFinancialReportUseCase) {
        this.generateFinancialReportUseCase = generateFinancialReportUseCase;
    }

    @GetMapping("/financial-pdf")
    public ResponseEntity<byte[]> downloadFinancialReport(
            @AuthenticationPrincipal Object principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        UUID userId = UUID.fromString(principal.toString());
        byte[] pdfContent = generateFinancialReportUseCase.execute(userId, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Define o header Content-Disposition para abrir no navegador ou forçar download ("attachment;")
        headers.setContentDispositionFormData("inline", "relatorio-financeiro.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}
