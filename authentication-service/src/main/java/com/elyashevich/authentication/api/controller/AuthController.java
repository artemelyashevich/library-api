package com.elyashevich.authentication.api.controller;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;
import com.elyashevich.authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody final AuthRequest request) {
        return this.authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody final AuthRequest request) {
        return this.authService.login(request);
    }
}
