package com.elyashevich.authentication.repository;

import com.elyashevich.authentication.entity.Role;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @ParameterizedTest
    @MethodSource("provideRole")
    void roleRepository_Save(final Role role, final String name) {
        // Act
        var savedRole = this.roleRepository.save(role);

        // Assert
        assertAll(
                () -> assertThat(savedRole).isNotNull(),
                () -> assertThat(savedRole.getId()).isNotNull(),
                () -> assertThat(savedRole.getName()).isEqualTo(name)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRole")
    void roleRepository_FindByName(final Role role, final String name) {
        // Act
        var newRole = this.roleRepository.findByName(name);

        // Assert
        assertAll(
                () -> assertThat(newRole).isNotNull(),
                () -> {
                    assert newRole != null;
                    assertThat(newRole.getName()).isEqualTo(name);
                }
        );
    }

    private static Stream<Arguments> provideRole() {
        var expectedName = "USER";

        var role = new Role();
        role.setName(expectedName);

        return Stream.of(
                Arguments.of(role, expectedName)
        );
    }
}
