package com.amalitech.usermanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;


public record LoginRequestPayload(
        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
