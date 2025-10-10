package com.bank.auth.authService.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UserDTO user;

    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}
