package com.bank.banking.bankingService.repositories;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountNumberOrToAccountNumber(Account fromAccount, Account toAccount);
    Optional<Transaction> findById(Long id);
    List<Transaction> findByFromAccountNumberAndToAccountNumber(Account fromAccount, Account toAccount);


}
