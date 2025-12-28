package com.service.transactionservice.service;

import com.service.transactionservice.entity.Transaction;
import com.service.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getTransactionRef() == null || transaction.getTransactionRef().isEmpty()) {
            transaction.setTransactionRef(generateTransactionRef());
        }
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Optional<Transaction> getTransactionByRef(String transactionRef) {
        return transactionRepository.findByTransactionRef(transactionRef);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactionRepository.findByFromAccountOrToAccount(accountNumber, accountNumber);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public Transaction updateTransactionStatus(Long id, Transaction.TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        transaction.setStatus(status);
        return transactionRepository.save(transaction);
    }

    public Transaction processDeposit(String accountNumber, Double amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setDescription(description);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setTransactionRef(generateTransactionRef());
        return transactionRepository.save(transaction);
    }

    public Transaction processWithdrawal(String accountNumber, Double amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setDescription(description);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setTransactionRef(generateTransactionRef());
        return transactionRepository.save(transaction);
    }

    public Transaction processTransfer(String fromAccount, String toAccount, Double amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setDescription(description);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setTransactionRef(generateTransactionRef());
        return transactionRepository.save(transaction);
    }

    private String generateTransactionRef() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
