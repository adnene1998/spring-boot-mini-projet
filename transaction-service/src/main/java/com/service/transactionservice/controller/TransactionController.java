package com.service.transactionservice.controller;

import com.service.transactionservice.entity.Transaction;
import com.service.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ref/{transactionRef}")
    public ResponseEntity<Transaction> getTransactionByRef(@PathVariable String transactionRef) {
        return transactionService.getTransactionByRef(transactionRef)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Transaction> updateTransactionStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusMap) {
        try {
            Transaction.TransactionStatus status = Transaction.TransactionStatus.valueOf(statusMap.get("status"));
            Transaction updatedTransaction = transactionService.updateTransactionStatus(id, status);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> processDeposit(@RequestBody Map<String, Object> depositRequest) {
        String accountNumber = (String) depositRequest.get("accountNumber");
        Double amount = Double.valueOf(depositRequest.get("amount").toString());
        String description = (String) depositRequest.get("description");
        Transaction transaction = transactionService.processDeposit(accountNumber, amount, description);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<Transaction> processWithdrawal(@RequestBody Map<String, Object> withdrawalRequest) {
        String accountNumber = (String) withdrawalRequest.get("accountNumber");
        Double amount = Double.valueOf(withdrawalRequest.get("amount").toString());
        String description = (String) withdrawalRequest.get("description");
        Transaction transaction = transactionService.processWithdrawal(accountNumber, amount, description);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> processTransfer(@RequestBody Map<String, Object> transferRequest) {
        String fromAccount = (String) transferRequest.get("fromAccount");
        String toAccount = (String) transferRequest.get("toAccount");
        Double amount = Double.valueOf(transferRequest.get("amount").toString());
        String description = (String) transferRequest.get("description");
        Transaction transaction = transactionService.processTransfer(fromAccount, toAccount, amount, description);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
