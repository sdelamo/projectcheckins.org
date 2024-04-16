package org.projectcheckins.security.http;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

@Singleton
class NewPasswordsMustMatchValidator implements ConstraintValidator<NewPasswordsMustMatch, PasswordForm> {

    @Override
    public boolean isValid(PasswordForm value, ConstraintValidatorContext context) {
        return Objects.equals(value.newPassword(), value.repeatNewPassword());
    }
}
