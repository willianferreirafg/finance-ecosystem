package com.finance.backend.core.domain.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("O e-mail '" + email + "' já está cadastrado no sistema.");
    }
}
