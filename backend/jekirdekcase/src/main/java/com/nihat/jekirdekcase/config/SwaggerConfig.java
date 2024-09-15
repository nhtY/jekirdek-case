package com.nihat.jekirdekcase.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("CRM-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .name("bearer-jwt")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addSecuritySchemes("custom-header",
                                new SecurityScheme()
                                        .name("custom-header")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-Custom-Header"))
                        .addSecuritySchemes("cookie-auth",
                                new SecurityScheme()
                                        .name("cookie-auth")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                                        .name("X-Cookie-Auth"))
                        .addHeaders("device-os",
                                new io.swagger.v3.oas.models.headers.Header()
                                        .description("The operating system of the device")
                                        .schema(new io.swagger.v3.oas.models.media.StringSchema())
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .addSecurityItem(new SecurityRequirement().addList("custom-header"))
                .addSecurityItem(new SecurityRequirement().addList("cookie-auth"));
    }

//@Bean
//public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("public-api")
//                .pathsToMatch("/api/**")
//                .addOpenApiCustomizer(openApi -> openApi
//                        .components(new io.swagger.v3.oas.models.Components()
//                                .addSecuritySchemes("cookieAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
//                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY)
//                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.COOKIE)
//                                        .name("X-Auth-Token")
//                                )
//                                .addParameters("X-Custom-Header", new io.swagger.v3.oas.models.parameters.Parameter()
//                                        .name("X-Custom-Header")
//                                        .description("A custom header used for API requests")
//                                        .required(true)
//                                        .schema(new io.swagger.v3.oas.models.media.Schema().type("string"))
//                                )
//                        )
//                        .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("cookieAuth"))
//                )
//                .build();
//    }
}
