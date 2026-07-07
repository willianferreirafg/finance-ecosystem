package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.exceptions.EmailAlreadyExistsException;
import com.finance.backend.core.domain.model.Category;
import com.finance.backend.core.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final CategoryRepositoryPort categoryRepository;

    public RegisterUserUseCaseImpl(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder, CategoryRepositoryPort categoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
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

        // Captura o usuário com o UUID real populado pelo banco de dados
        User savedUser = userRepository.save(newUser);

        // Cria as categorias padrão de finanças pessoais atreladas a este usuário
        List<Category> defaultCategories = List.of(
                Category.builder().name("Alimentação").icon("utensils").color("#EF4444").user(savedUser).build(),
                Category.builder().name("Moradia").icon("home").color("#3B82F6").user(savedUser).build(),
                Category.builder().name("Transporte").icon("car").color("#F59E0B").user(savedUser).build(),
                Category.builder().name("Lazer").icon("party-popper").color("#10B981").user(savedUser).build(),
                Category.builder().name("Salário").icon("wallet").color("#22C55E").user(savedUser).build(),
                Category.builder().name("Investimentos").icon("trending-up").color("#8B5CF6").user(savedUser).build()
        );

        categoryRepository.saveAll(defaultCategories);
    }
}
