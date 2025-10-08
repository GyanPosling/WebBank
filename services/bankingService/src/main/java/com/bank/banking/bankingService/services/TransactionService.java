package com.bank.banking.bankingService.services;

import com.bank.banking.bankingService.models.Account;
import com.bank.banking.bankingService.models.Transaction;
import com.bank.banking.bankingService.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
     public void save(Transaction transaction) {
        transactionRepository.save(transaction);
     }

     public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
     }

     public List<Transaction> findByFromAccountNumberOrToAccountNumber(Account fromAccount, Account toAccount) {
        return transactionRepository.findByFromAccountNumberOrToAccountNumber(fromAccount, toAccount);
     }

    public List<Transaction> findByFromAccountNumberAndToAccountNumber(Account fromAccount, Account toAccount) {
        return transactionRepository.findByFromAccountNumberAndToAccountNumber(fromAccount, toAccount);
    }
}
