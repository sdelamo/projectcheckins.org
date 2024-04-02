package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.forms.AnswerSave;

import java.util.List;

public interface AnswerService {

    @NonNull
    String save(@NotNull Authentication authentication, @NotNull @Valid AnswerSave answerSave, @Nullable Tenant tenant);

    @NonNull
    List<? extends Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant);
}
