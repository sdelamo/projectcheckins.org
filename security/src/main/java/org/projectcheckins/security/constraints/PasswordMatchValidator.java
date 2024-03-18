package org.projectcheckins.security.constraints;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Introspected
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RepeatPasswordForm> {
    @Override
    public boolean isValid(RepeatPasswordForm value, ConstraintValidatorContext context) {
        if (value.password() == null) {
            return value.repeatPassword() == null;
        }
        return value.password().equals(value.repeatPassword());
    }
}
