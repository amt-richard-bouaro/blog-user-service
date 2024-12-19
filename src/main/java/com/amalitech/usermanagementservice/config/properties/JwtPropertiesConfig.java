package com.amalitech.usermanagementservice.config.properties;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtPropertiesConfig(
        @NotBlank
        String tokenSecretKey,

        @Min(value = 30000, message = "Token expiration must be at least 30,000 ms")
        @Max(value = 10000000, message = "Token expiration must not exceed 10,000,000 ms")
        Long tokenExpiration
) {
}
