package com.examly.springapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * This class configures Swagger for API documentation.
 * It sets up the OpenAPI specification with information about the API,
 * contact details, and security schemes.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates an OpenAPI bean with API information, contact details, and security requirements.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Food Mart")
                    .version("1.0.0")
                    .contact(new Contact()
                        .name("Demo API Support")
                        .email("vipulkanjolia@gmail.com")
                        .url("#"))
                    .description("Maintaining food service."))
                .addSecurityItem(new SecurityRequirement()
                    .addList("Bearer Authentication"))
                .components(new Components()
                    .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * Creates a SecurityScheme for JWT Bearer Authentication.
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
    }
}
