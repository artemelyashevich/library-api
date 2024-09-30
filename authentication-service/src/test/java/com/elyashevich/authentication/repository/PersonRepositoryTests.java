package com.elyashevich.authentication.repository;

import com.elyashevich.authentication.entity.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class PersonRepositoryTests {

    @Autowired
    private PersonRepository personRepository;

    @ParameterizedTest
    @MethodSource("providePerson")
    void personRepository_Save(final Person person, final String expectedEmail) {
        // Act
        var savedPerson = this.personRepository.save(person);

        // Assert
        assertAll(
                () -> assertThat(savedPerson).isNotNull(),
                () -> assertThat(savedPerson.getId()).isNotNull(),
                () -> assertThat(savedPerson.getEmail()).isEqualTo(expectedEmail)
        );
    }

    @Test
    void personRepository_FindAll() {
        // Act
        var people = this.personRepository.findAll();

        // Assert
        assertAll(
                () -> assertThat(people).isNotNull(),
                () -> assertThat(people.size()).isEqualTo(2)
        );
    }

    @ParameterizedTest
    @MethodSource("providePerson")
    void personRepository_FindByEmail(final Person person, final String expectedEmail) {
        // Act
        this.personRepository.save(person);
        var actualPerson = this.personRepository.findByEmail(expectedEmail).get();

        // Assert
        assertAll(
                () -> assertThat(actualPerson).isNotNull(),
                () -> assertThat(actualPerson.getEmail()).isEqualTo(expectedEmail)
        );
    }

    @ParameterizedTest
    @MethodSource("providePerson")
    void personRepository_ExistsByEmail(final Person person, final String expectedEmail) {
        // Act
        this.personRepository.save(person);
        var isExists = this.personRepository.existsByEmail(expectedEmail);

        // Assert
        assertThat(isExists).isTrue();
    }

    private static Stream<Arguments> providePerson() {
        var expectedEmail = "example@example.com";
        var person = new Person();
        person.setEmail(expectedEmail);

        return Stream.of(
                Arguments.of(person, expectedEmail)
        );
    }
}
