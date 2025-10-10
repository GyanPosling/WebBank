package com.bank.auth.authService.utils;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
