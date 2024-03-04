package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface EmailConfirmationTokenGenerator {

    @NonNull
    String generateToken(@NonNull @NotBlank @Email String email);
}
