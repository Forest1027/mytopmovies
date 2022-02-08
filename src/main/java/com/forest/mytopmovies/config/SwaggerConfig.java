package com.forest.mytopmovies.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(components());
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("Authorization-Token", securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
    }
}
