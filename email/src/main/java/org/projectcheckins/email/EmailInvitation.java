package org.projectcheckins.email;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Introspected
public record EmailInvitation(@NonNull @NotBlank @Email String email,
                              @NonNull @NotBlank String url) {
}
