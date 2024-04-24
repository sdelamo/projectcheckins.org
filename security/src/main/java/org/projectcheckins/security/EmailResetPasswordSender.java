package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

public interface EmailResetPasswordSender {
    void sendResetPasswordEmail(@NonNull @NotBlank @Email String email,
                                @NonNull @NotBlank String url,
                                @NonNull @NotNull Locale locale);
}
