package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.UniqueInvitation;
import org.projectcheckins.security.constraints.UserDoesNotExist;

@Serdeable
@UserDoesNotExist
@UniqueInvitation
public record TeamInvitationRecord(@NonNull @NotBlank @Email String email,
                                   @Nullable Tenant tenant) implements TeamInvitation {
}
