package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@FunctionalInterface
public interface EmailConfirmationRepository {

    void enableByEmail(@NonNull @NotBlank @Email String email);
}
