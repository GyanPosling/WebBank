package com.bank.banking.bankingService.controllers;

import com.bank.banking.bankingService.models.Transaction;
import com.bank.banking.bankingService.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public CompletableFuture<ResponseEntity<Transaction>> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.processTransactionAsync(transaction)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Transaction>>> getAllTransactions() {
        return transactionService.getAllTransactionsAsync()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Transaction>> getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionByIdAsync(id)
                .thenApply(transaction ->
                        transaction.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                );
    }

    @GetMapping("/account/{accountNumber}")
    public CompletableFuture<ResponseEntity<List<Transaction>>> getTransactionsByAccount(@PathVariable String accountNumber) {
        return transactionService.getTransactionsByAccountAsync(accountNumber)
                .thenApply(ResponseEntity::ok);
    }
}