package com.bank.banking.bankingService.services;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        account.setAccountNumber(generateAccountNumber());
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public boolean accountExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).isPresent();
    }

    @Async
    public CompletableFuture<List<Account>> getAllAccountsAsync() {
        return CompletableFuture.completedFuture(accountRepository.findAll());
    }

    @Async
    public CompletableFuture<Optional<Account>> getAccountByNumberAsync(String accountNumber) {
        return CompletableFuture.completedFuture(accountRepository.findByAccountNumber(accountNumber));
    }

    @Async
    public CompletableFuture<List<Account>> getAccountsByUserIdAsync(Long userId) {
        return CompletableFuture.completedFuture(accountRepository.findByUserId(userId));
    }

    @Async
    public CompletableFuture<BigDecimal> getUserTotalBalanceAsync(Long userId) {
        return CompletableFuture.supplyAsync(() ->
                accountRepository.findByUserId(userId).stream()
                        .map(Account::getBalance)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
}