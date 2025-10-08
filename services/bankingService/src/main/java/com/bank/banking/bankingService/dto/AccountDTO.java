package com.bank.banking.bankingService.dto;

import com.bank.banking.bankingService.models.enums.AccountType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class AccountDTO {
    private int id;
    private String accountNumber;
    private Long userId;
    private AccountType accontType;
    private double balance;
    private LocalDateTime createdAt;
}
