package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.usecases.CreateTransactionUseCase;
import com.finance.backend.core.usecases.FetchTransactionsUseCase;
import com.finance.backend.core.usecases.GetTransactionSummaryUseCase;
import com.finance.backend.infrastructure.entrypoints.dto.TransactionRequestDto;
import com.finance.backend.infrastructure.entrypoints.dto.TransactionResponseDto;
import com.finance.backend.infrastructure.entrypoints.dto.TransactionSummaryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.finance.backend.core.usecases.UserRepositoryPort;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final FetchTransactionsUseCase fetchTransactionsUseCase;
    private final GetTransactionSummaryUseCase getTransactionSummaryUseCase;
    private final UserRepositoryPort userRepository;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, FetchTransactionsUseCase fetchTransactionsUseCase, GetTransactionSummaryUseCase getTransactionSummaryUseCase, UserRepositoryPort userRepository) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.fetchTransactionsUseCase = fetchTransactionsUseCase;
        this.getTransactionSummaryUseCase = getTransactionSummaryUseCase;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(
            @Valid @RequestBody TransactionRequestDto request,
            @AuthenticationPrincipal Object principal) {

        // busca o UUID por e-mail para não quebrar no POST
        String email = principal.toString();
        UUID userId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Transaction transaction = createTransactionUseCase.execute(
                userId,
                request.categoryId(),
                request.description(),
                request.amount(),
                request.date(),
                request.type(),
                request.paid()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponseDto.fromDomain(transaction));
    }

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryDto> getSummary(@AuthenticationPrincipal Object principal) {
        String email = principal.toString();
        UUID userId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        TransactionSummaryDto summary = getTransactionSummaryUseCase.execute(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> listAll(@AuthenticationPrincipal Object principal) {
        String email = principal.toString();
        UUID userId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        List<TransactionResponseDto> list = fetchTransactionsUseCase.execute(userId).stream()
                .map(TransactionResponseDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
}
