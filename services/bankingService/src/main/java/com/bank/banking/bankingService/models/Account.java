package com.bank.banking.bankingService.models;

import com.bank.banking.bankingService.models.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "userId")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private AccountType accontType;

    private BigDecimal balance;

    private LocalDateTime createdAt;


}
