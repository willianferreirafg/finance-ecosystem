package com.finance.backend.infrastructure.config;

import com.finance.backend.infrastructure.database.entities.UserEntity;
import com.finance.backend.infrastructure.database.repositories.SpringDataUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final SpringDataUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DatabaseSeeder(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String testEmail = "admin@finance.com";

        if (userRepository.findByEmail(testEmail).isEmpty()) {
            var defaultUser = UserEntity.builder()
                    .name("Administrador Sênior")
                    .email(testEmail)
                    // "admin123" criptografado com BCrypt
                    .password(passwordEncoder.encode("admin123"))
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(defaultUser);
            System.out.println("✅ [SEED] Usuário de teste criado com sucesso: admin@finance.com / admin123");
        }
    }
}
