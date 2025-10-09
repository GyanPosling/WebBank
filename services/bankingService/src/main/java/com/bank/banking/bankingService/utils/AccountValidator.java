package com.bank.banking.bankingService.utils;

import com.bank.banking.bankingService.models.Account;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account account = (Account) target;

        if (account.getUserId() == null) {
            errors.rejectValue("userId", "ID пользователя обязательно");
        }

        if (account.getBalance() == null || account.getBalance().compareTo(java.math.BigDecimal.ZERO) < 0) {
            errors.rejectValue("balance", "Баланс не может быть отрицательным");
        }
    }
}