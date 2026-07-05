package com.finance.backend.core.domain.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credenciais inválidas. Verifique seu e-mail e senha.");
    }
}
