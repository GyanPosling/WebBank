package com.bank.auth.authService.services;

import com.bank.auth.authService.dto.UserDTO;
import com.bank.auth.authService.models.User;
import com.bank.auth.authService.repositories.UserRepository;
import com.bank.auth.authService.security.JwtUtil;
import com.bank.auth.authService.utils.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO register(User user) {
        log.info("Registering user: {}", user.getEmail());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AuthException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getId());
        return convertToUserDTO(savedUser);
    }

    public String login(String email, String password) {
        log.info("Login attempt: {}", email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return jwtUtil.generateToken(authentication.getName());
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO getUserDTOByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));
        return convertToUserDTO(user);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}