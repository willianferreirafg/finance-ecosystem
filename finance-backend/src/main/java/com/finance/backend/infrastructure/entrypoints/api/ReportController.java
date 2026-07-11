package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.usecases.GenerateFinancialReportUseCase;
import com.finance.backend.core.usecases.UserRepositoryPort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final GenerateFinancialReportUseCase generateFinancialReportUseCase;
    private final UserRepositoryPort userRepository;

    public ReportController(GenerateFinancialReportUseCase generateFinancialReportUseCase, UserRepositoryPort userRepository) {
        this.generateFinancialReportUseCase = generateFinancialReportUseCase;
        this.userRepository = userRepository;
    }

    @GetMapping("/financial-pdf")
    public ResponseEntity<byte[]> downloadFinancialReport(
            @AuthenticationPrincipal Object principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 1. Pegamos o e-mail que está vindo no principal do Spring Security
        String email = principal.toString();

        // 2. Buscamos o UUID no banco de dados usando o e-mail
        UUID userId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // 3. Executamos o caso de uso gerando o relatório com o UUID correto
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
