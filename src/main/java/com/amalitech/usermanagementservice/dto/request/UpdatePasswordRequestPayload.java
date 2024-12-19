package com.amalitech.usermanagementservice.dto.request;

import com.amalitech.usermanagementservice.validation.ChangePasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@ChangePasswordsMatch
public record UpdatePasswordRequestPayload(
        @NotBlank(message = "Current password cannot be blank")
        String currentPassword,

        @NotBlank(message = "New password cannot be blank")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$&!]).*$",
                message = "New password must include uppercase, lowercase, a number, and a special character"
        )
        String newPassword,

        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword
) {
}
