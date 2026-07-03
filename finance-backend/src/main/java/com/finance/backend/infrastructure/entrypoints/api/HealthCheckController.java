package com.finance.backend.infrastructure.entrypoints.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/health")
public class HealthCheckController {

    // Uso de Record para DTOs internos e imutáveis - Prática Sênior
    private record HealthResponse(String status, String architecture, Instant timestamp) {}

    @GetMapping
    public ResponseEntity<HealthResponse> check() {
        var response = new HealthResponse(
                "UP",
                "Clean Architecture + Spring Boot 4 active",
                Instant.now()
        );
        return ResponseEntity.ok(response);
    }
}
