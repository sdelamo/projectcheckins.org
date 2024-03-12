package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record QuestionUpdate(@NotBlank @InputHidden String id,
                             @NotBlank String title,
                             @NotBlank String schedule) {
}
