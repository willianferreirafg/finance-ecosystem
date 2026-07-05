package com.finance.backend.core.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private UUID id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;
    private boolean paid;
    private User user;
    private Category category;
}
