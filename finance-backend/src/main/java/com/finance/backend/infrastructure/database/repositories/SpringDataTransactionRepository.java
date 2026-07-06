package com.finance.backend.infrastructure.database.repositories;

import com.finance.backend.infrastructure.database.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
}
