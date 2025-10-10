package com.bank.auth.authService.controller;


import com.bank.auth.authService.dto.AuthRequest;
import com.bank.auth.authService.dto.AuthResponse;
import com.bank.auth.authService.dto.UserDTO;
import com.bank.auth.authService.models.User;
import com.bank.auth.authService.services.AuthService;
import com.bank.auth.authService.utils.ErrorsUtil;
import com.bank.auth.authService.utils.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserValidator userValidator;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user, BindingResult bindingResult) {
        log.info("Register request: {}", user.getEmail());

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnErrorsToClient(bindingResult);
        }

        UserDTO userDTO = authService.register(user);
        String token = authService.login(user.getEmail(), user.getPassword());
        AuthResponse response = new AuthResponse(token, userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, BindingResult bindingResult) {
        log.info("Login request: {}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "Email обязателен");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "Пароль обязателен");
        }
        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnErrorsToClient(bindingResult);
        }

        String token = authService.login(request.getEmail(), request.getPassword());
        UserDTO userDTO = authService.getUserDTOByEmail(request.getEmail());
        AuthResponse response = new AuthResponse(token, userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody Map<String, String> request) {
        boolean isValid = authService.validateToken(request.get("token"));
        return ResponseEntity.ok(isValid);
    }
}