package com.bank.banking.bankingService.dto;

import com.bank.banking.bankingService.models.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountDTO {
    private int id;
    private String accountNumber;
    private Long userId;
    private AccountType accountType;
    private double balance;
    private LocalDateTime createdAt;
}
