package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record QuestionSave(@NotBlank String title,
                           @NotBlank String schedule) {

}
