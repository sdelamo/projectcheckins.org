package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.AnswerSave;

public interface AnswerRepository {
    void save(@NonNull @NotNull Authentication authentication,
              @NonNull @NotNull @Valid AnswerSave answerSave);
}
