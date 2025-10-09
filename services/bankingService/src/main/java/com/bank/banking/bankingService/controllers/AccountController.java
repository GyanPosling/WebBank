package com.bank.banking.bankingService.controllers;

import com.bank.banking.bankingService.dto.AccountDTO;
import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.services.AccountService;
import com.bank.banking.bankingService.utils.AccountValidator;
import com.bank.banking.bankingService.utils.ErrorsUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountValidator accountValidator;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO, BindingResult bindingResult) {
        Account account = convertToAccount(accountDTO);

        accountValidator.validate(account, bindingResult);
        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnErrorsToClient(bindingResult);
        }

        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(convertToAccountDTO(createdAccount));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<AccountDTO>>> getAllAccounts() {
        return accountService.getAllAccountsAsync()
                .thenApply(accounts -> ResponseEntity.ok(accounts.stream()
                        .map(this::convertToAccountDTO)
                        .collect(Collectors.toList())));
    }

    @GetMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<AccountDTO>> getAccount(@PathVariable String accountNumber) {
        return accountService.getAccountByNumberAsync(accountNumber)
                .thenApply(account ->
                        account.map(acc -> ResponseEntity.ok(convertToAccountDTO(acc)))
                                .orElse(ResponseEntity.notFound().build())
                );
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<AccountDTO>>> getUserAccounts(@PathVariable Long userId) {
        return accountService.getAccountsByUserIdAsync(userId)
                .thenApply(accounts -> ResponseEntity.ok(accounts.stream()
                        .map(this::convertToAccountDTO)
                        .collect(Collectors.toList())));
    }

    @GetMapping("/user/{userId}/balance")
    public CompletableFuture<ResponseEntity<BigDecimal>> getUserTotalBalance(@PathVariable Long userId) {
        return accountService.getUserTotalBalanceAsync(userId)
                .thenApply(ResponseEntity::ok);
    }

    private Account convertToAccount(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }

    private AccountDTO convertToAccountDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }
}