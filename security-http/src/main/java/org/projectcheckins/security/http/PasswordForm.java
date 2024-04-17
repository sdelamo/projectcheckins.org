package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import io.micronaut.views.fields.annotations.InputPassword;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.PasswordMatch;
import org.projectcheckins.security.constraints.RepeatPasswordForm;

@Serdeable
@CheckCurrentPassword
@PasswordMatch
public record PasswordForm(@NonNull @NotBlank @InputHidden String userId,
                           @NonNull @NotBlank @InputPassword String currentPassword,
                           @NonNull @NotBlank @InputPassword String password,
                           @NonNull @NotBlank @InputPassword String repeatPassword) implements RepeatPasswordForm {

    PasswordForm(@NonNull Authentication authentication) {
        this(authentication.getName(), null, null, null);
    }
}
