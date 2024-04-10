package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Serdeable
public record AnswerUpdateRecord(@NotNull @PastOrPresent LocalDate answerDate,
                                 @NotNull Format format,
                                 @NotBlank String content
) implements AnswerUpdate {
}
