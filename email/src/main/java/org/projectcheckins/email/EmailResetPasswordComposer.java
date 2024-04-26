package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

@FunctionalInterface
public interface EmailResetPasswordComposer {

    @NonNull
    Email.Builder composeEmailResetPassword(@NonNull @NotNull Locale locale,
                                            @NonNull @NotNull @Valid EmailResetPassword emailResetPassword);
}
