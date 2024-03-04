package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@FunctionalInterface
public interface EmailConfirmationTokenValidator {

    @NonNull
    Optional<Authentication> validate(@NonNull @NotBlank String token);
}
