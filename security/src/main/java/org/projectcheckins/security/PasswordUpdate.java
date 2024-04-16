package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record PasswordUpdate(@NonNull @NotBlank String userId,
                             @NonNull @NotBlank String newEncodedPassword) {
}
