package com.project.HeartConnect.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Heart Connect",
                version = "1.0.0",
                description = "Dating Application",
                contact = @Contact(
                        name = "Heart Connect",
                        email = "heartconnect@gmail.com"
                )
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        },
        externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(
                url = "src/main/resources/swagger.yml" // Reference to your external YAML file
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        description = "JWT Authentication",
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
