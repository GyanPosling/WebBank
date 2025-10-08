package com.bank.banking.bankingService.services;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.models.Transaction;
import com.bank.banking.bankingService.repositories.AccountRepository;
import com.bank.banking.bankingService.repositories.TransactionRepository;
import com.bank.banking.bankingService.utils.AccountNotFoundException;
import com.bank.banking.bankingService.utils.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    public CompletableFuture<Transaction> processTransactionAsync(Transaction transaction) {
        return CompletableFuture.supplyAsync(() -> {
            Account fromAccount = accountRepository.findByAccountNumber(transaction.getFromAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("From account not found"));

            Account toAccount = accountRepository.findByAccountNumber(transaction.getToAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("To account not found"));

            if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new TransactionException("Insufficient funds");
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transaction.setTransactionId(UUID.randomUUID().toString());
            transaction.setCreatedAt(LocalDateTime.now());

            Transaction savedTransaction = transactionRepository.save(transaction);

            kafkaTemplate.send("transaction-events", savedTransaction);

            return savedTransaction;
        });
    }

    @Async
    public CompletableFuture<List<Transaction>> getAllTransactionsAsync() {
        return CompletableFuture.completedFuture(transactionRepository.findAll());
    }

    @Async
    public CompletableFuture<Optional<Transaction>> getTransactionByIdAsync(Long id) {
        return CompletableFuture.completedFuture(transactionRepository.findById(id));
    }

    @Async
    public CompletableFuture<List<Transaction>> getTransactionsByAccountAsync(String accountNumber) {
        return CompletableFuture.completedFuture(
                transactionRepository.findByFromAccountNumberOrToAccountNumber(accountNumber, accountNumber)
        );
    }

//    @Async
//    public CompletableFuture<Boolean> validateAccountsAsync(String fromAccount, String toAccount) {
//        return CompletableFuture.supplyAsync(() ->
//                accountService.accountExists(fromAccount) &&
//                        accountService.accountExists(toAccount)
//        );
//    }
}