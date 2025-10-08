package com.bank.banking.bankingService.services;

import com.bank.banking.bankingService.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTransactionProcessor {

    @Autowired
    private AccountRepository accountRepository;

    @Async
    public CompletableFuture<Boolean> validateAccountsAsync(String fromAccount, String toAccount) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> fromAccountCheck = CompletableFuture.supplyAsync(() ->
                    accountRepository.findByAccountNumber(fromAccount).isPresent());

            CompletableFuture<Boolean> toAccountCheck = CompletableFuture.supplyAsync(() ->
                    accountRepository.findByAccountNumber(toAccount).isPresent());

            return fromAccountCheck.join() && toAccountCheck.join();
        });
    }
}
