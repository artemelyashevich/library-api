package com.elyashevich.authentication.service.converter;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonConverter {

    public Person convertToPerson(AuthRequest authRequest) {
        return Person
                .builder()
                .email(authRequest.email())
        .build();
    }
}
