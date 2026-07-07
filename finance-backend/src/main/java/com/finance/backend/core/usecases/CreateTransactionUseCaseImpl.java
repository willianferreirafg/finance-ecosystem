package com.finance.backend.core.usecases;

import com.finance.backend.core.domain.exceptions.ResourceNotFoundException;
import com.finance.backend.core.domain.model.Category;
import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.domain.model.TransactionType;
import com.finance.backend.core.domain.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final UserRepositoryPort userRepository; // Para fins de validação / segurança de posse
    private final CategoryRepositoryPort categoryRepository;

    public CreateTransactionUseCaseImpl(TransactionRepositoryPort transactionRepository,
                                        UserRepositoryPort userRepository,
                                        CategoryRepositoryPort categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Transaction execute(UUID userId, UUID categoryId, String description, BigDecimal amount, LocalDate date, TransactionType type, boolean paid) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID fornecido."));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("A categoria informada não pertence ao usuário logado.");
        }

        User user = category.getUser();

        Transaction newTransaction = Transaction.builder()
                .id(null)
                .description(description)
                .amount(amount)
                .date(date)
                .type(type)
                .paid(paid)
                .user(user)
                .category(category)
                .build();

        return transactionRepository.save(newTransaction);
    }
}
