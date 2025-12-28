package com.service.transactionservice.repository;

import com.service.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionRef(String transactionRef);
    List<Transaction> findByFromAccount(String fromAccount);
    List<Transaction> findByToAccount(String toAccount);
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
}
