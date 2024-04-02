package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.api.Answer;

import java.time.LocalDate;

@Serdeable
public record AnswerRecord(
        @NotBlank(groups = Saved.class) String id,
        @NotBlank String questionId,
        @NotBlank String respondentId,
        @NotNull @PastOrPresent LocalDate answerDate,
        @NotNull Format format,
        @NotBlank String text
) implements Answer {
}
