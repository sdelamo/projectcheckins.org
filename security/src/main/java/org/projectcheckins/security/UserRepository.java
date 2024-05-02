package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface UserRepository {
    boolean existsByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);

    void deleteByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);

    void updateAuthorities(@NotBlank @Email String email,
                           @NonNull List<String> authorities,
                           @Nullable Tenant tenant);
}
