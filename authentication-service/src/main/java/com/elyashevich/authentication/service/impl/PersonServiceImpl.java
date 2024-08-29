package com.elyashevich.authentication.service.impl;

import com.elyashevich.authentication.api.dto.AuthRequest;
import com.elyashevich.authentication.entity.Person;
import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.exception.ResourceNotFoundException;
import com.elyashevich.authentication.repository.PersonRepository;
import com.elyashevich.authentication.repository.RoleRepository;
import com.elyashevich.authentication.service.PersonService;
import com.elyashevich.authentication.service.converter.PersonConverter;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements UserDetailsService, PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;
    private final PersonConverter personConverter;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var person = this.personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such user with email: %s.".formatted(email)));
        return new User(
                person.getEmail(),
                person.getPassword(),
                getMappedRolesToSimpleGrantedAuthority(person.getRoles())
        );
    }

    private static List<SimpleGrantedAuthority> getMappedRolesToSimpleGrantedAuthority(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public Person create(AuthRequest authRequest) {
        var person = this.personConverter.convertToPerson(authRequest);
        person.setPassword(
                this.encoder.encode(authRequest.password())
        );
        person.setRoles(Set.of(
                this.roleRepository.findByName("ROLE_USER")
        ));
        return this.personRepository.save(person);
    }
}
