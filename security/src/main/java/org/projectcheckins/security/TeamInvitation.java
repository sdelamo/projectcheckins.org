package org.projectcheckins.security;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.UniqueInvitation;
import org.projectcheckins.security.constraints.UserDoesNotExist;

public interface TeamInvitation {
    @Nullable
    Tenant tenant();

    @Email
    @NotBlank
    String email();
}
