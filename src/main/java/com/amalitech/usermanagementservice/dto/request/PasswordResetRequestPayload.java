package com.amalitech.usermanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestPayload(
        @NotBlank(message = "Username is required")
        String username
) {
}
