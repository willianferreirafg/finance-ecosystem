package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.usecases.AuthenticateUserUseCase;
import com.finance.backend.core.usecases.RefreshTokenUseCase;
import com.finance.backend.core.usecases.RegisterUserUseCase;
import com.finance.backend.infrastructure.entrypoints.dto.AuthDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    private final RefreshTokenUseCase refreshTokenUseCase;

    private final RegisterUserUseCase registerUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase, RefreshTokenUseCase refreshTokenUseCase, RegisterUserUseCase registerUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.registerUserUseCase = registerUserUseCase;
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

    @PostMapping("/refresh")
    public ResponseEntity<AuthDto.LoginResponse> refresh(@Valid @RequestBody AuthDto.RefreshRequest request) {
        var result = refreshTokenUseCase.execute(request.refreshToken());

        var response = new AuthDto.LoginResponse(
                result.accessToken(),
                result.refreshToken(),
                "Bearer",
                result.refreshTokenExpiresAt()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthDto.RegisterRequest request) {
        registerUserUseCase.execute(request.name(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
