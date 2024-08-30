package com.elyashevich.authentication.service.impl;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.exception.ResourceAlreadyExistsException;
import com.elyashevich.authentication.exception.ResourceNotFoundException;
import com.elyashevich.authentication.repository.PersonRepository;
import com.elyashevich.authentication.repository.RoleRepository;
import com.elyashevich.authentication.service.PersonService;
import com.elyashevich.authentication.service.converter.PersonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements UserDetailsService, PersonService {

    private static final String ROLE_USER = "ROLE_USER";

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;
    private final PersonConverter personConverter;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        log.debug("Attempting to load user with email: '{}'.", email);
        var person = this.personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such user with email: %s.".formatted(email)));

        log.info("User with email '{}' has been found.", email);
        return new User(
                person.getEmail(),
                person.getPassword(),
                getMappedRolesToSimpleGrantedAuthority(person.getRoles())
        );
    }

    @Override
    @Transactional
    public void create(final AuthRequest authRequest) {
        log.debug("Attempting to create a new user with email: '{}'.", authRequest.email());
        if (personRepository.existsByEmail(authRequest.email())) {
            throw new ResourceAlreadyExistsException("Such user already exists.");
        }

        var person = this.personConverter.convertToPerson(authRequest);
        person.setPassword(
                this.encoder.encode(authRequest.password())
        );
        person.setRoles(Set.of(
                this.roleRepository.findByName(ROLE_USER)
        ));

        log.info("User with email '{}' has been created.", authRequest.email());
        this.personRepository.save(person);
    }

    private static List<SimpleGrantedAuthority> getMappedRolesToSimpleGrantedAuthority(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
}
