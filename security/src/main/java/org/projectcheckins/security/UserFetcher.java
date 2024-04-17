package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public interface UserFetcher {

    @NonNull
    Optional<UserState> findById(@NotBlank @NonNull String id);

    @NonNull
    Optional<UserState> findByEmail(@NotBlank @NonNull String email);
}
