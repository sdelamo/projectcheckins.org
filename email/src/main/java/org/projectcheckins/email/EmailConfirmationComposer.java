package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;

@FunctionalInterface
public interface EmailConfirmationComposer {

    @NonNull
    Email.Builder composeEmailConfirmation(@NonNull @NotNull Locale locale,
                                           @NonNull @NotNull @Valid EmailConfirmation emailConfirmation);
}
