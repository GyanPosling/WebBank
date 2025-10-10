package com.bank.notification.notificationService.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionEvent {
    private String transactionId;
    private String type;
    private BigDecimal amount;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String description;
    private LocalDateTime createdAt;
}