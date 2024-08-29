package com.elyashevich.authentication.service;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;

public interface AuthService {

    AuthResponse register(final AuthRequest request);

    AuthResponse login(final AuthRequest request);
}
