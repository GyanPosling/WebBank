package com.bank.banking.bankingService.utils;

import com.bank.banking.bankingService.models.Transaction;
import com.bank.banking.bankingService.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TransactionValidator implements Validator {

    private final AccountService accountService;

    @Autowired
    public TransactionValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Transaction.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Transaction transaction = (Transaction) target;

        if (transaction.getFromAccountNumber() == null || transaction.getToAccountNumber() == null) {
            errors.rejectValue("fromAccountNumber", "Номера счетов не могут быть пустыми");
            return;
        }

        if (!accountService.accountExists(transaction.getFromAccountNumber())) {
            errors.rejectValue("fromAccountNumber", "Счет отправителя не существует");
        }

        if (!accountService.accountExists(transaction.getToAccountNumber())) {
            errors.rejectValue("toAccountNumber", "Счет получателя не существует");
        }

        if (transaction.getFromAccountNumber().equals(transaction.getToAccountNumber())) {
            errors.rejectValue("toAccountNumber", "Нельзя переводить на тот же счет");
        }

        if (transaction.getAmount() == null || transaction.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            errors.rejectValue("amount", "Сумма должна быть положительной");
        }
    }
}
