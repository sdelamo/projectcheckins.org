package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface AuthoritiesFetcher {

    @NonNull
    List<String> findAuthoritiesByEmail(@NonNull @NotBlank String email);
}
