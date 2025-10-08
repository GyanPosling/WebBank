package com.bank.banking.bankingService.services;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateAccount(Account account){
        Account accountToUpdate = new Account();
        accountToUpdate.setAccountNumber(account.getAccountNumber());
        accountToUpdate.setBalance(account.getBalance());
        //accountToUpdate.setAccountType(account.getAccountType());
        accountToUpdate.setUserId(account.getUserId());
        accountToUpdate.setCreatedAt(account.getCreatedAt());
        accountRepository.save(accountToUpdate);
    }

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    public BigDecimal getUserTotalBalance(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
