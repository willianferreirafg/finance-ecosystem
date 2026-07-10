package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.usecases.GenerateFinancialReportUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenerateFinancialReportUseCase generateFinancialReportUseCase;

    @Test
    @WithMockUser // Simula um usuário autenticado no Spring Security
    void shouldReturnPdfReportWithCorrectHeaders() throws Exception {
        UUID mockUserId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);
        byte[] mockPdfBytes = "%PDF-1.4 ... dados ficticios do pdf".getBytes();

        // Configura o comportamento do Mock para aceitar qualquer UUID enviado pelo controlador
        Mockito.when(generateFinancialReportUseCase.execute(Mockito.any(UUID.class), Mockito.eq(startDate), Mockito.eq(endDate)))
                .thenReturn(mockPdfBytes);

        mockMvc.perform(get("/api/reports/financial-pdf")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"relatorio-financeiro.pdf\""))
                .andExpect(content().bytes(mockPdfBytes));
    }
}
