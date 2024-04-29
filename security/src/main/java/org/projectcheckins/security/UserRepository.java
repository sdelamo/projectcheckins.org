package org.projectcheckins.security;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository {
    boolean existsByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);

    void deleteByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);

}
