package com.bank.banking.bankingService.utils;


public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
