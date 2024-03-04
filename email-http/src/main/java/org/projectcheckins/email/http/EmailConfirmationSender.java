package org.projectcheckins.email.http;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

public interface EmailConfirmationSender {
    void sendConfirmationEmail(@NonNull @NotBlank @Email String email);

    void sendConfirmationEmail(@NonNull @NotBlank @Email String email,
                               @NonNull @NotBlank String host,
                               @NonNull @NotNull Locale locale);
}
