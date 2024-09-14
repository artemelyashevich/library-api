package com.elyashevich.authentication;

import com.elyashevich.authentication.entity.Person;
import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.exception.ResourceNotFoundException;
import com.elyashevich.authentication.repository.PersonRepository;
import com.elyashevich.authentication.service.converter.PersonConverter;
import com.elyashevich.authentication.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Test class for testing the PersonService.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PersonServiceTests {
    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonRepository repository;

    @Mock
    private PersonConverter converter;

    /**
     * Test case for testing the loadByUsername method with an existing person.
     */
    @Test
    public void loadByUsername_ExistingPerson() {
        var personEmail = "example@example.com";
        var dummyPerson = getPersonExample(personEmail);

        when(this.repository.findByEmail(personEmail)).thenReturn(Optional.of(dummyPerson));
        assertEquals(dummyPerson.getEmail(), new User(
                dummyPerson.getEmail(),
                dummyPerson.getPassword(),
                getMappedRolesToSimpleGrantedAuthority(dummyPerson.getRoles())
        ).getUsername());
    }

    /**
     * Test case for testing the loadByUsername method when it throws a ResourceNotFoundException.
     */
    @Test
    public void testGetById_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> this.personService.loadUserByUsername(""));
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
