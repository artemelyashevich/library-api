package com.elyashevich.authentication.service;

import com.elyashevich.authentication.api.dto.AuthRequest;

/**
 * The PersonService interface defines methods for managing person-related operations.
 */
public interface PersonService {

    /**
     * Creates a new person based on the information provided in the AuthRequest.
     *
     * @param authRequest The AuthRequest object containing information for creating a new person.
     */
    void create(AuthRequest authRequest);
}
