package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.usecases.AuthenticateUserUseCase;
import com.finance.backend.infrastructure.entrypoints.dto.AuthDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto.LoginResponse> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        var result = authenticateUserUseCase.execute(request.email(), request.password());

        var response = new AuthDto.LoginResponse(
                result.accessToken(),
                result.refreshToken(),
                "Bearer",
                result.refreshTokenExpiresAt()
        );

        return ResponseEntity.ok(response);
    }
}
