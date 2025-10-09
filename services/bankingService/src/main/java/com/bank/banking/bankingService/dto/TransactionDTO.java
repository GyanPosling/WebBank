package com.bank.banking.bankingService.dto;

import com.bank.banking.bankingService.models.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private String transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
    private String fromAccountNumber;
    private String toAccountNumber;

    public boolean isValid() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}