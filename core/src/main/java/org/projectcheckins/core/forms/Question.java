package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record Question(@NotBlank String id,
                       @NotBlank String title,
                       @NotBlank String schedule) {
}
