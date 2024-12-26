package com.amalitech.usermanagementservice.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * OpenAPI Configuration class for the User Management Service of the Blog API.
 * This class provides metadata for the API documentation including contact information,
 * license, description, title, and server details.
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Java Upskilling Lab",
                        email = "support@amalitech.com",
                        url = "https://amalitech.org"
                ),
                description = "This documentation provides the detailed API specifications for the User Management Service " +
                        "of the Blog API. It includes all the available endpoints, their request and response formats, " +
                        "and other relevant details.",
                title = "Blog API - User Management",
                version = "1.0.0",
                license = @License(
                        name = "AMALITECH",
                        url = "https://amalitech.org"
                )
        ),
        servers = {
                @Server(
                        description = "API Gateway",
                        url = "http://localhost:7001"
                ),
                @Server(
                        description = "Development Server",
                        url = "http://localhost:7002"
                ),
                @Server(
                        description = "Staging Server",
                        url = "http://blog.app.local"
                )
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
)
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT Bearer token used for API authorization",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
    // No additional configuration is required here as the annotations provide all necessary details.
}