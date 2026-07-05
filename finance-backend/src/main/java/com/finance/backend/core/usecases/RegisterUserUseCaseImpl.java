package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.exceptions.EmailAlreadyExistsException;
import com.finance.backend.core.domain.model.User;
import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterUserUseCaseImpl(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(String name, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        // passamos null no ID porque quem manda na geração do identificador de persistência é o banco
        User newUser = new User(null, name, email, hashedPassword, LocalDateTime.now());

        userRepository.save(newUser);
    }
}
