package com.elyashevich.authentication.service;

import com.elyashevich.authentication.entity.Person;
import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.exception.ResourceNotFoundException;
import com.elyashevich.authentication.repository.PersonRepository;
import com.elyashevich.authentication.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Test class for testing the PersonService.
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceTests {

    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonRepository repository;

    @ParameterizedTest
    @MethodSource("providePerson")
    void personService_LoadByUsername(final Person person, final String email) {
        // Arrange
        when(this.repository.findByEmail(email)).thenReturn(Optional.of(person));

        // Act
        var actualPerson = this.personService.loadUserByUsername(email);

        // Assert
        assertEquals(person.getEmail(), actualPerson.getUsername());
    }

    @ParameterizedTest
    @EmptySource
    void personService_LoadUserByUsername_throwsException(final String email) {
        // Assert
        assertThrows(ResourceNotFoundException.class, () -> this.personService.loadUserByUsername(email));
    }

    private static Stream<Arguments> providePerson() {
        var email = "example@example.com";
        var role = new Role();
        role.setName("USER");
        var person = new Person(
                UUID.randomUUID(),
                email,
                "123456789",
                Set.of(role),
                null,
                null
        );
        return Stream.of(
                Arguments.of(person, email)
        );
    }
}
