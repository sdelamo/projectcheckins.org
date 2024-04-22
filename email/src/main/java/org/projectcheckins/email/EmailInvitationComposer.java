package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

@FunctionalInterface
public interface EmailInvitationComposer {

    @NonNull
    Email.Builder composeEmailInvitation(@NonNull @NotNull Locale locale,
                                         @NonNull @NotNull @Valid EmailInvitation emailInvitation);
}
