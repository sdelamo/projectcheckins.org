package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Singleton
class EmailConfirmationTokenValidatorImpl implements EmailConfirmationTokenValidator {
    private final TokenValidator<?> tokenValidator;

    EmailConfirmationTokenValidatorImpl(TokenValidator<?> tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    @NonNull
    public Optional<Authentication> validate(@NonNull @NotBlank String token) {
        return Mono.from(tokenValidator.validateToken(token, null)).blockOptional();
    }
}
