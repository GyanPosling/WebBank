package com.bank.banking.bankingService.services;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        log.info("Creating account for user: {}", account.getUserId());
        account.setAccountNumber(generateAccountNumber());
        account.setCreatedAt(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully: {}", savedAccount.getAccountNumber());
        return savedAccount;
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        log.debug("Fetching account by number: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        log.debug("Fetching accounts for user: {}", userId);
        return accountRepository.findByUserId(userId);
    }

    public boolean accountExists(String accountNumber) {
        log.debug("Checking if account exists: {}", accountNumber);
        boolean exists = accountRepository.findByAccountNumber(accountNumber).isPresent();
        log.debug("Account {} exists: {}", accountNumber, exists);
        return exists;
    }

    @Async
    public CompletableFuture<List<Account>> getAllAccountsAsync() {
        log.info("Fetching all accounts asynchronously");
        return CompletableFuture.completedFuture(accountRepository.findAll());
    }

    @Async
    public CompletableFuture<Optional<Account>> getAccountByNumberAsync(String accountNumber) {
        log.debug("Fetching account asynchronously: {}", accountNumber);
        return CompletableFuture.completedFuture(accountRepository.findByAccountNumber(accountNumber));
    }

    @Async
    public CompletableFuture<List<Account>> getAccountsByUserIdAsync(Long userId) {
        log.debug("Fetching user accounts asynchronously: {}", userId);
        return CompletableFuture.completedFuture(accountRepository.findByUserId(userId));
    }

    @Async
    public CompletableFuture<BigDecimal> getUserTotalBalanceAsync(Long userId) {
        log.debug("Calculating total balance for user: {}", userId);
        return CompletableFuture.supplyAsync(() ->
                accountRepository.findByUserId(userId).stream()
                        .map(Account::getBalance)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private String generateAccountNumber() {
        String accountNumber = "ACC" + System.currentTimeMillis();
        log.debug("Generated account number: {}", accountNumber);
        return accountNumber;
    }
}