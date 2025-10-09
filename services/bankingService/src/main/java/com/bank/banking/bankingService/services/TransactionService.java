package com.bank.banking.bankingService.services;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.models.Transaction;
import com.bank.banking.bankingService.repositories.AccountRepository;
import com.bank.banking.bankingService.repositories.TransactionRepository;
import com.bank.banking.bankingService.utils.AccountNotFoundException;
import com.bank.banking.bankingService.utils.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    public CompletableFuture<Transaction> processTransactionAsync(Transaction transaction) {
        log.info("Processing transaction from {} to {}", transaction.getFromAccountNumber(), transaction.getToAccountNumber());
        return CompletableFuture.supplyAsync(() -> {
            Account fromAccount = accountRepository.findByAccountNumber(transaction.getFromAccountNumber())
                    .orElseThrow(() -> {
                        log.error("From account not found: {}", transaction.getFromAccountNumber());
                        return new AccountNotFoundException("From account not found");
                    });

            Account toAccount = accountRepository.findByAccountNumber(transaction.getToAccountNumber())
                    .orElseThrow(() -> {
                        log.error("To account not found: {}", transaction.getToAccountNumber());
                        return new AccountNotFoundException("To account not found");
                    });

            if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                log.error("Insufficient funds for account: {}. Balance: {}, Required: {}",
                        transaction.getFromAccountNumber(), fromAccount.getBalance(), transaction.getAmount());
                throw new TransactionException("Insufficient funds");
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transaction.setTransactionId(UUID.randomUUID().toString());
            transaction.setCreatedAt(LocalDateTime.now());

            Transaction savedTransaction = transactionRepository.save(transaction);
            log.info("Transaction completed successfully: {}", savedTransaction.getTransactionId());

            kafkaTemplate.send("transaction-events", savedTransaction);
            log.debug("Transaction event sent to Kafka: {}", savedTransaction.getTransactionId());

            return savedTransaction;
        });
    }

    @Async
    public CompletableFuture<List<Transaction>> getAllTransactionsAsync() {
        log.info("Fetching all transactions asynchronously");
        return CompletableFuture.completedFuture(transactionRepository.findAll());
    }

    @Async
    public CompletableFuture<Optional<Transaction>> getTransactionByIdAsync(Long id) {
        log.debug("Fetching transaction by id: {}", id);
        return CompletableFuture.completedFuture(transactionRepository.findById(id));
    }

    @Async
    public CompletableFuture<List<Transaction>> getTransactionsByAccountAsync(String accountNumber) {
        log.debug("Fetching transactions for account: {}", accountNumber);
        return CompletableFuture.completedFuture(
                transactionRepository.findByFromAccountNumberOrToAccountNumber(accountNumber, accountNumber)
        );
    }

    @Async
    public CompletableFuture<Void> validateTransactionAsync(Transaction transaction) {
        log.debug("Validating transaction: {} -> {}", transaction.getFromAccountNumber(), transaction.getToAccountNumber());
        return CompletableFuture.runAsync(() -> {
            if (!accountService.accountExists(transaction.getFromAccountNumber())) {
                log.error("From account not found during validation: {}", transaction.getFromAccountNumber());
                throw new AccountNotFoundException("From account not found");
            }
            if (!accountService.accountExists(transaction.getToAccountNumber())) {
                log.error("To account not found during validation: {}", transaction.getToAccountNumber());
                throw new AccountNotFoundException("To account not found");
            }
            if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Invalid transaction amount: {}", transaction.getAmount());
                throw new TransactionException("Amount must be positive");
            }
            log.debug("Transaction validation passed");
        });
    }
}