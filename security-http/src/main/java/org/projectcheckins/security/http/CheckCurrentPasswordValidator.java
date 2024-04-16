package org.projectcheckins.security.http;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectcheckins.security.PasswordEncoder;
import org.projectcheckins.security.UserFetcher;

@Singleton
class CheckCurrentPasswordValidator implements ConstraintValidator<CheckCurrentPassword, PasswordForm> {

    private final UserFetcher userFetcher;
    private final PasswordEncoder passwordEncoder;

    CheckCurrentPasswordValidator(UserFetcher userFetcher, PasswordEncoder passwordEncoder) {
        this.userFetcher = userFetcher;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isValid(PasswordForm value, ConstraintValidatorContext context) {
        return userFetcher.findById(value.userId())
                .map(u -> passwordEncoder.matches(value.currentPassword(), u.getPassword()))
                .orElse(false);
    }
}
