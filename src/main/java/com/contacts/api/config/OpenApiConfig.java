package com.contacts.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI contactsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Contacts API")
                        .description("RESTful API for managing contacts with Spring Boot and MongoDB")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("mark_t_womack@hotmail.com")))
                .servers(List.of(
                        new Server().url("/").description("Default Server")
                ));
    }
}
