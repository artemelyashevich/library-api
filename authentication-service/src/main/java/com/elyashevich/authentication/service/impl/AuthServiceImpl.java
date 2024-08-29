package com.elyashevich.authentication.service.impl;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;
import com.elyashevich.authentication.service.AuthService;
import com.elyashevich.authentication.service.impl.PersonServiceImpl;
import com.elyashevich.authentication.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonServiceImpl personService;

    @Override
    public AuthResponse register(AuthRequest request) {
        this.personService.create(request);
        var userDetails = this.personService.loadUserByUsername(request.email());
        return new AuthResponse(TokenUtil.generateToken(userDetails));
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        var userDetails = this.personService.loadUserByUsername(request.email());
        return new AuthResponse(this.createToken(userDetails));
    }

    @Override
    public String validate(String token) {
        return TokenUtil.validate(token);
    }

    private String createToken(UserDetails userDetails) {
        return TokenUtil.generateToken(userDetails);
    }
}
