package com.amalitech.usermanagementservice.validation;

import com.amalitech.usermanagementservice.dto.request.UpdatePasswordRequestPayload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChangePasswordMatchValidator implements ConstraintValidator<ChangePasswordsMatch, UpdatePasswordRequestPayload> {

    @Override
    public boolean isValid(UpdatePasswordRequestPayload payload, ConstraintValidatorContext context) {
        if (payload == null) {
            return true;
        }

        boolean passwordsMatch = payload.newPassword().equals(payload.confirmPassword());
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
