package com.amalitech.usermanagementservice.dto.request;

import com.amalitech.usermanagementservice.exceptions.BadRequestException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequestPayload(

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, message = "Username must be at least 3 characters long")
        String username,

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @Pattern(
                regexp = "^\\+?[0-9. ()-]{7,15}$",
                message = "Phone number must be a valid format"
        )
        String phone
) {


}
