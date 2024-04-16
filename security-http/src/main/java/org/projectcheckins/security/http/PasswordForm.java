package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import io.micronaut.views.fields.annotations.InputPassword;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@CheckCurrentPassword
@NewPasswordsMustMatch
public record PasswordForm(@NonNull @NotBlank @InputHidden String userId,
                           @NonNull @NotBlank @InputPassword String currentPassword,
                           @NonNull @NotBlank @InputPassword String newPassword,
                           @NonNull @NotBlank @InputPassword String repeatNewPassword) {

    PasswordForm(@NonNull Authentication authentication) {
        this(authentication.getName(), null, null, null);
    }
}
