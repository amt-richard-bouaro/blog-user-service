package com.amalitech.usermanagementservice.validation;

import com.amalitech.usermanagementservice.dto.request.RegistrationRequestPayload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, RegistrationRequestPayload> {

    @Override
    public boolean isValid(RegistrationRequestPayload payload, ConstraintValidatorContext context) {
        if (payload == null) {
            return true;
        }

        boolean passwordsMatch = payload.password().equals(payload.confirmPassword());
        if (!passwordsMatch) {
            // Disable default violation message
            context.disableDefaultConstraintViolation();

            // Attach the error to the 'confirmPassword' field
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
