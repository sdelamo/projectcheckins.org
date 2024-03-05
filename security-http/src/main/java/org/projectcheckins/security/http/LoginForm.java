package org.projectcheckins.security.http;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Serdeable
public record LoginForm(
        /**
         * This must be username to match {@link UsernamePasswordCredentials#getUsername()}
         */
        @NotBlank @Email String username,
        @InputPassword @NotBlank String password) {
}
