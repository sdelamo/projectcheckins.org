package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface RegisterService {
    @NonNull String register(@NonNull @NotBlank String username,
                             @NonNull @NotBlank String rawPassword) throws UserAlreadyExistsException;

    @NonNull String register(@NonNull @NotBlank String username,
                    @NonNull @NotBlank String rawPassword,
                    @NonNull List<String> authorities) throws UserAlreadyExistsException;

    @NonNull
    String register(@NonNull @NotNull @Valid UserSave userSave) throws UserAlreadyExistsException;
}
