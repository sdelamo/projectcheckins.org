package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import io.micronaut.views.fields.annotations.TrixEditor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Serdeable
@OnlyOncePerDay
public record AnswerWysiwygSave(@NotBlank @InputHidden String questionId,
                                @NotBlank @InputHidden String respondentId,
                                @NotNull @PastOrPresent LocalDate answerDate,
                                @NotBlank @TrixEditor String html
) implements AnswerForm {
}
