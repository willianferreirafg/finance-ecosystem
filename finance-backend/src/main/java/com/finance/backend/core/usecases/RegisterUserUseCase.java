package com.finance.backend.core.usecases;

import org.springframework.stereotype.Component;

@Component
public interface RegisterUserUseCase {
    void execute(String name, String email, String rawPassword);
}
