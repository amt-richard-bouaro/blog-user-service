package com.amalitech.usermanagementservice.dto.request;

import com.amalitech.usermanagementservice.validation.PasswordsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordsMatch(message = "Passwords must match")
public record RegistrationRequestPayload(

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, message = "Username must be at least 3 characters long")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$&!]).*$",
                message = "Password must include uppercase, lowercase, a number, and a special character"
        )
        String password,

        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword,

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
