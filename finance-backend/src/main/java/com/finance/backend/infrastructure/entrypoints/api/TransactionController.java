package com.finance.backend.infrastructure.entrypoints.api;

import com.finance.backend.core.domain.model.Transaction;
import com.finance.backend.core.usecases.CreateTransactionUseCase;
import com.finance.backend.infrastructure.entrypoints.dto.TransactionRequestDto;
import com.finance.backend.infrastructure.entrypoints.dto.TransactionResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(
            @Valid @RequestBody TransactionRequestDto request,
            @AuthenticationPrincipal Object principal) {

        UUID userId = UUID.fromString(principal.toString());

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
}
