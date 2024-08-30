package com.elyashevich.authentication.service;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;

/**
 * The AuthService interface defines methods for handling user authentication.
 */
public interface AuthService {

    /**
     * Registers a new user based on the information provided in the AuthRequest.
     *
     * @param request The AuthRequest object containing user registration details.
     * @return An AuthResponse object indicating the result of the registration process.
     */
    AuthResponse register(final AuthRequest request);

    /**
     * Logs in a user using the credentials provided in the AuthRequest.
     *
     * @param request The AuthRequest object containing user login credentials.
     * @return An AuthResponse object indicating the result of the login process.
     */
    AuthResponse login(final AuthRequest request);

    /**
     * Validates a given authentication token.
     *
     * @param token The authentication token to be validated.
     * @return A String indicating the result of the token validation process.
     */
    String validate(final String token);
}
