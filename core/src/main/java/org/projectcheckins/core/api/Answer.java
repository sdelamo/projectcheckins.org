package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.Saved;

import java.time.LocalDate;

public interface Answer {

    @NotBlank(groups = Saved.class) String id();
    @NotBlank String questionId();
    @NotBlank String respondentId();
    @NotNull @PastOrPresent LocalDate answerDate();
    @NotNull Format format();
    @NotBlank String text();
}
