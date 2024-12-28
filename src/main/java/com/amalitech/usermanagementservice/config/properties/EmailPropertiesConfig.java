package com.amalitech.usermanagementservice.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.mail")
public record EmailPropertiesConfig(
        @NotBlank(message = "Mail username cannot be blank") String username

) {

}
