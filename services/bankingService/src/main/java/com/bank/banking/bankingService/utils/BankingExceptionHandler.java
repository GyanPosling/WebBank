package com.bank.banking.bankingService.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BankingExceptionHandler {

    @ExceptionHandler(BankingException.class)
    public ResponseEntity<BankingErrorResponse> handleException(BankingException e) {
        BankingErrorResponse response = new BankingErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<BankingErrorResponse> handleException(AccountNotFoundException e) {
        BankingErrorResponse response = new BankingErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<BankingErrorResponse> handleException(TransactionException e) {
        BankingErrorResponse response = new BankingErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BankingErrorResponse> handleException(Exception e) {
        BankingErrorResponse response = new BankingErrorResponse(
                "Внутренняя ошибка сервера: " + e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
