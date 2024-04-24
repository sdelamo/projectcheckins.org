package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@FunctionalInterface
public interface BlockingTokenValidator {
    @NonNull
    Optional<Authentication> validateToken(@NonNull @NotBlank String token);
}
