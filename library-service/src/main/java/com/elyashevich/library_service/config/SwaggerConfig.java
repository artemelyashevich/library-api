package com.elyashevich.library_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${application.open-api.email}")
    private String email;

    @Value("${application.open-api.server}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url(this.serverUrl)
                        )
                )
                .info(
                        new Info()
                                .title("Library service")
                                .description("This is a sample API documentation for library service using Swagger")
                                .version("1.0")
                                .contact(new Contact().email(this.email))
                );
    }
}
