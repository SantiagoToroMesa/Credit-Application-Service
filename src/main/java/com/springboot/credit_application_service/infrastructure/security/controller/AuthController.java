package com.springboot.credit_application_service.infrastructure.security.controller;

import com.springboot.credit_application_service.infrastructure.security.model.UserEntity;
import com.springboot.credit_application_service.infrastructure.security.repository.UserRepository;
import com.springboot.credit_application_service.infrastructure.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered!";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user) {
        UserEntity u = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(user.getPassword(), u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(u.getUsername());
    }
}