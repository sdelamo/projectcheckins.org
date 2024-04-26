package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import io.micronaut.views.fields.annotations.InputPassword;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.PasswordMatch;
import org.projectcheckins.security.constraints.RepeatPasswordForm;
import org.projectcheckins.security.constraints.ValidToken;

@Serdeable
@PasswordMatch
public record ResetPasswordForm(@NonNull @NotBlank @ValidToken @InputHidden String token,
                                @NonNull @NotBlank @InputPassword String password,
                                @NonNull @NotBlank @InputPassword String repeatPassword) implements RepeatPasswordForm {

    ResetPasswordForm(@NonNull @ValidToken String token) {
        this(token, null, null);
    }
}
