package org.projectcheckins.security.constraints;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectcheckins.security.BlockingTokenValidator;

import java.util.Optional;

@Singleton
public class ValidTokenValidator implements ConstraintValidator<ValidToken, String> {

    private final BlockingTokenValidator blockingTokenValidator;

    public ValidTokenValidator(BlockingTokenValidator blockingTokenValidator) {
        this.blockingTokenValidator = blockingTokenValidator;
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {
        return Optional.ofNullable(token).flatMap(blockingTokenValidator::validateToken).isPresent();
    }
}
