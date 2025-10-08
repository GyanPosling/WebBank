package com.bank.banking.bankingService.controllers;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Account>>> getAllAccounts() {
        return accountService.getAllAccountsAsync()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<Account>> getAccount(@PathVariable String accountNumber) {
        return accountService.getAccountByNumberAsync(accountNumber)
                .thenApply(account ->
                        account.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                );
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<Account>>> getUserAccounts(@PathVariable Long userId) {
        return accountService.getAccountsByUserIdAsync(userId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/user/{userId}/balance")
    public CompletableFuture<ResponseEntity<BigDecimal>> getUserTotalBalance(@PathVariable Long userId) {
        return accountService.getUserTotalBalanceAsync(userId)
                .thenApply(ResponseEntity::ok);
    }
}
