package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UserEntity(@NotBlank String id,
                         @NotBlank String email,
                         @NotBlank String encodedPassword,
                         @NonNull List<String> authorities) {
 }
