package com.finance.backend.core.usecases;

import org.springframework.stereotype.Component;

@Component
public interface PasswordEncoderPort {
    boolean matches(String rawPassword, String encodedPassword);
    String encode(String rawPassword);
}
