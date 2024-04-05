package org.projectcheckins.core.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@OnlyOncePerDay
public interface AnswerForm {
    @NotBlank String respondentId();
    @NotBlank String questionId();
    @NotNull @PastOrPresent LocalDate answerDate();
}
