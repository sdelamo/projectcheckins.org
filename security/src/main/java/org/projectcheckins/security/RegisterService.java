package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

public interface RegisterService {

    @NonNull
    default String register(@NonNull @NotBlank String email,
                            @NonNull @NotBlank String rawPassword,
                            @Nullable Tenant tenant) throws RegistrationCheckViolationException {
        return register(email, rawPassword, tenant, Collections.emptyList());
    }

    @NonNull String register(@NonNull @NotBlank String email,
                    @NonNull @NotBlank String rawPassword,
                    @Nullable Tenant tenant,
                    @NonNull List<String> authorities) throws RegistrationCheckViolationException;
}
