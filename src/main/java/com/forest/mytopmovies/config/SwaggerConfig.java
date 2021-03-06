package com.forest.mytopmovies.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(@Value("${product.version}") String appVersion) {
        return new OpenAPI()
                .components(components())
                .info(new Info().title("MyTopMovies API").version(appVersion)
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("Authorization-Token", securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
    }
}
