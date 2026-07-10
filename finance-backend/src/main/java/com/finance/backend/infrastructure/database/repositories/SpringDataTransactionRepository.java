package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.infrastructure.database.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);
}
