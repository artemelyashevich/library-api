package com.elyashevich.authentication;

import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthenticationServiceApplication {

	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

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
