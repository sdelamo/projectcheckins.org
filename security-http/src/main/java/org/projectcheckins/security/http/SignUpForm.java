package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.PasswordMatch;
import org.projectcheckins.security.constraints.RepeatPasswordForm;

@PasswordMatch
@Serdeable
public record SignUpForm(@NonNull @NotBlank @Email String email,
                         @NonNull @NotBlank @InputPassword String password,
                         @NonNull @NotBlank @InputPassword String repeatPassword) implements RepeatPasswordForm {
}
