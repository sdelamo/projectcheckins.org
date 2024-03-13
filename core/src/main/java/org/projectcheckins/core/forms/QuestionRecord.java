package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.core.api.Question;

@Serdeable
public record QuestionRecord(
        @NotBlank String id,
        @NotBlank String title,
        @NotBlank String schedule
) implements Question {
}
