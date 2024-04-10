package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import io.micronaut.views.fields.annotations.Textarea;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Serdeable
public record AnswerUpdateMarkdown(@NotNull @InputHidden @PastOrPresent LocalDate answerDate,
                                   @NotNull @InputHidden Format format,
                                   @NotBlank @Textarea String content) implements AnswerUpdate {
    public AnswerUpdateMarkdown(@NotNull @PastOrPresent LocalDate answerDate, @NotBlank String content) {
        this(answerDate, Format.MARKDOWN, content);
    }
}
