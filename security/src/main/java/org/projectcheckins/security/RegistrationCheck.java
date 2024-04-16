package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.order.Ordered;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;

public interface RegistrationCheck extends Ordered {

    /**
     *
     * @param email The email address to register.
     * @return An empty optional if the registration is possible with the supplied email address.
     */
    @NonNull
    Optional<RegistrationCheckViolation> validate(@NotBlank @Email String email, @Nullable Tenant tenant);
}
