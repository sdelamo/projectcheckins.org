package org.projectcheckins.core.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public interface AnswerUpdate {
    @NotNull @PastOrPresent LocalDate answerDate();
    @NotNull Format format();
    @NotBlank String content();
}
