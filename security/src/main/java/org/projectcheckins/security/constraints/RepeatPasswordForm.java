package org.projectcheckins.security.constraints;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

public interface RepeatPasswordForm {
    @NonNull
    @NotBlank
    String password();

    @NonNull
    @NotBlank
    String repeatPassword();
}
