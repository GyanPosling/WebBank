package com.bank.banking.bankingService.controllers;

import com.bank.banking.bankingService.dto.TransactionDTO;
import com.bank.banking.bankingService.models.Transaction;
import com.bank.banking.bankingService.services.TransactionService;
import com.bank.banking.bankingService.utils.TransactionValidator;
import com.bank.banking.bankingService.utils.ErrorsUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransactionValidator transactionValidator;

    @PostMapping("/transfer")
    public CompletableFuture<ResponseEntity<TransactionDTO>> createTransaction(@RequestBody TransactionDTO transactionDTO, BindingResult bindingResult) {
        Transaction transaction = convertToTransaction(transactionDTO);

        transactionValidator.validate(transaction, bindingResult);
        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnErrorsToClient(bindingResult);
        }

        return transactionService.validateTransactionAsync(transaction)
                .thenCompose(voidResult -> transactionService.processTransactionAsync(transaction))
                .thenApply(savedTransaction -> ResponseEntity.ok(convertToTransactionDTO(savedTransaction)));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getAllTransactions() {
        return transactionService.getAllTransactionsAsync()
                .thenApply(transactions -> ResponseEntity.ok(transactions.stream()
                        .map(this::convertToTransactionDTO)
                        .collect(Collectors.toList())));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<TransactionDTO>> getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionByIdAsync(id)
                .thenApply(transaction ->
                        transaction.map(t -> ResponseEntity.ok(convertToTransactionDTO(t)))
                                .orElse(ResponseEntity.notFound().build())
                );
    }

    @GetMapping("/account/{accountNumber}")
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getTransactionsByAccount(@PathVariable String accountNumber) {
        return transactionService.getTransactionsByAccountAsync(accountNumber)
                .thenApply(transactions -> ResponseEntity.ok(transactions.stream()
                        .map(this::convertToTransactionDTO)
                        .collect(Collectors.toList())));
    }

    private Transaction convertToTransaction(TransactionDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);
    }

    private TransactionDTO convertToTransactionDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }
}