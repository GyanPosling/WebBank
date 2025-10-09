package com.bank.banking.bankingService.repositories;


import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountNumberOrToAccountNumber(String fromAccount, String toAccount);
    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE t.fromAccountNumber = :accountNumber OR t.toAccountNumber = :accountNumber ORDER BY t.createdAt DESC")
    List<Transaction> findRecentTransactionsByAccount(@Param("accountNumber") String accountNumber, Pageable pageable);
}