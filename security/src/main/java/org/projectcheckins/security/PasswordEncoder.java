package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

public interface PasswordEncoder {
    @NonNull
    String encode(@NotBlank String rawPassword);

    boolean matches(@NotBlank String rawPassword,
                    @NotBlank String encodedPassword);
}
