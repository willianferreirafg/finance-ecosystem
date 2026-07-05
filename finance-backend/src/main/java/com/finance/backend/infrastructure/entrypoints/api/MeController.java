package com.finance.backend.infrastructure.entrypoints.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class MeController {

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal String email) {
        // O @AuthenticationPrincipal injeta o email que o JwtAuthenticationFilter extraiu do token
        return ResponseEntity.ok(Map.of(
                "email", email,
                "role", "USER",
                "status", "AUTHENTICATED_SUCCESSFULLY"
        ));
    }
}
