package org.projectcheckins.security.api;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface PublicProfile {

    @NotBlank String id();

    @NotBlank @Email String email();

    @NonNull
    String fullName();
}
