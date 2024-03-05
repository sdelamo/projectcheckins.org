package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;

public interface RegisterService {

    @NonNull
    default String register(@NonNull @NotBlank String email,
                             @NonNull @NotBlank String rawPassword) throws UserAlreadyExistsException {
        return register(email, rawPassword, Collections.emptyList());
    }

    @NonNull String register(@NonNull @NotBlank String email,
                    @NonNull @NotBlank String rawPassword,
                    @NonNull List<String> authorities) throws UserAlreadyExistsException;
}
