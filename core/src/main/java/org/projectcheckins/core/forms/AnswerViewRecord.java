package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.AnswerView;
import org.projectcheckins.core.api.PublicProfile;

@Serdeable
public record AnswerViewRecord (
        @NotNull Answer answer,
        @NotNull PublicProfile respondent,
        @NotBlank String html,
        boolean isEditable
) implements AnswerView {
}
