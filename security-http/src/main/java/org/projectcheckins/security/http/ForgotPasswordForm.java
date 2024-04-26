package org.projectcheckins.security.http;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record ForgotPasswordForm(@NotBlank @Email String email) {
}
