package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.Form;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record MemberRow(@NonNull @NotNull @Email String email,
                        @NonNull @NotNull String fullName,
                        @Nullable Form deleteForm) {
}
