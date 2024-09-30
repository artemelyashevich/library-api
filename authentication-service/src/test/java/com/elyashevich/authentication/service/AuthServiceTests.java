package com.elyashevich.authentication.service;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.entity.Person;
import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.service.impl.AuthServiceImpl;
import com.elyashevich.authentication.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Test class for testing the PersonService.
 */
@ExtendWith(MockitoExtension.class)
 class AuthServiceTests {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private PersonServiceImpl personService;

    /**
     * Test case for testing the register method.
     */
    @Test
    @Transactional
     void register() {
        // Setting up test data
        var request = getAuthRequestExample();
        when(personService.loadUserByUsername(request.email())).thenReturn(
                new User(
                        request.email(),
                        request.password(),
                        getMappedRolesToSimpleGrantedAuthority(getPersonExample(request.email()).getRoles())
                )
        );

        var response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.token());
    }

    /**
     * Test case for testing the login method.
     */
    @Test
    @Transactional
     void login() {
        var request = getAuthRequestExample();
        when(personService.loadUserByUsername(request.email())).thenReturn(
                new User(
                        request.email(),
                        request.password(),
                        getMappedRolesToSimpleGrantedAuthority(getPersonExample(request.email()).getRoles())
                )
        );

        var response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.token());
    }

    private static AuthRequest getAuthRequestExample() {
        return new AuthRequest(
                "example@example.com",
                "123456789"
        );
    }

    private Person getPersonExample(String personEmail) {
        var role = new Role();
        role.setName("USER");
        return new Person(
                UUID.randomUUID(),
                "123456789",
                personEmail,
                Set.of(role),
                null,
                null
        );
    }

    private static List<SimpleGrantedAuthority> getMappedRolesToSimpleGrantedAuthority(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
}