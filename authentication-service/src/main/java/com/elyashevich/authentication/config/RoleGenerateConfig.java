package com.elyashevich.authentication.config;

import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleGenerateConfig {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Bean
    public CommandLineRunner CommandLineRunnerBean(final RoleRepository roleRepository) {
        return (args) -> {
            if (!roleRepository.existsByName(ROLE_USER)) {
                var role = new Role();
                role.setName(ROLE_USER);
                roleRepository.save(role);
            }
            if (!roleRepository.existsByName(ROLE_ADMIN)) {
                var role = new Role();
                role.setName(ROLE_ADMIN);
                roleRepository.save(role);
            }
        };
    }
}
