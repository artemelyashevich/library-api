package com.elyashevich.authentication.service;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.api.dto.AuthResponse;
import com.elyashevich.authentication.entity.Person;

public interface PersonService {

    Person create(AuthRequest authRequest);
}
