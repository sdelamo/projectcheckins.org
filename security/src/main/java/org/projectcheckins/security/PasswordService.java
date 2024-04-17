package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

public interface PasswordService {

    void updatePassword(@NonNull @NotBlank String userId,
                        @NonNull @NotBlank String newRawPassword);
}
