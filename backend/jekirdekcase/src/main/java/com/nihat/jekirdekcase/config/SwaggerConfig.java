package com.nihat.jekirdekcase.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(openApi -> openApi
                        .components(new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes("cookieAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY)
                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.COOKIE)
                                        .name("X-Auth-Token")
                                )
                        )
                        .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("cookieAuth"))
                )
                .build();
    }

}
