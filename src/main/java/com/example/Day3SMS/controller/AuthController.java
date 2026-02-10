package com.example.Day3SMS.controller;

import com.example.Day3SMS.dto.LoginRequestDto;
import com.example.Day3SMS.dto.RegisterRequestDto;
import com.example.Day3SMS.dto.TokenResponseDto;
import com.example.Day3SMS.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public TokenResponseDto login(
            @RequestBody LoginRequestDto dto
    ) {
        return service.login(dto);
    }

    @PostMapping("/register")
    public TokenResponseDto register(
            @Valid @RequestBody RegisterRequestDto dto
    ) {
        return service.register(dto);
    }
}