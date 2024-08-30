package com.elyashevich.authentication;

import com.elyashevich.authentication.entity.Role;
import com.elyashevich.authentication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class AuthenticationServiceApplication {

	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	private final RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner CommandLineRunnerBean() {
		return (args) -> {
			if (!this.roleRepository.existsByName(ROLE_USER)) {
				var role = new Role();
				role.setName(ROLE_USER);
				this.roleRepository.save(role);
			}
			if (!this.roleRepository.existsByName(ROLE_ADMIN)) {
				var role = new Role();
				role.setName(ROLE_ADMIN);
				this.roleRepository.save(role);
			}
		};
	}
}
