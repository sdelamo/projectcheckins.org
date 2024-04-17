package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.api.PublicProfile;

public interface AnswerView {

    @NotNull Answer answer();
    @NotNull PublicProfile respondent();
    @NotBlank String html();
    boolean isEditable();
}
