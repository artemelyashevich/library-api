package com.elyashevich.authentication.service.impl;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;
import com.elyashevich.authentication.service.AuthService;
import com.elyashevich.authentication.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonServiceImpl personService;

    @Override
    @Transactional
    public AuthResponse register(final AuthRequest request) {
        log.debug("Attempting to register user with email: '{}'.", request.email());

        this.personService.create(request);
        var userDetails = this.personService.loadUserByUsername(request.email());

        log.info("User with email '{}' has been registered.", request.email());
        return new AuthResponse(TokenUtil.generateToken(userDetails));
    }

    @Override
    public AuthResponse login(final AuthRequest request) {
        log.debug("Attempting to login user with email: '{}'.", request.email());

        var userDetails = this.personService.loadUserByUsername(request.email());

        log.info("User with email '{}' has been logged in.", request.email());
        return new AuthResponse(createToken(userDetails));
    }

    @Override
    public String validate(final String token) {
        return TokenUtil.validate(token);
    }

    private static String createToken(final UserDetails userDetails) {
        return TokenUtil.generateToken(userDetails);
    }
}
