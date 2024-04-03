package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.Saved;

import java.time.LocalDate;

public interface AnswerView {

    @NotNull Answer answer();
    @NotNull PublicProfile respondent();
    @NotBlank String html();
    boolean isEditable();
}
