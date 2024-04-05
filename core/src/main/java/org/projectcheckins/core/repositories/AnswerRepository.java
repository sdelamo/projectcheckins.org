package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {
    @NotBlank
    String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant);

    @NonNull
    Optional<? extends Answer> findById(@NotBlank String id, @Nullable Tenant tenant);

    @NonNull
    List<? extends Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant);

    @NonNull
    List<? extends Answer> findByQuestionIdAndRespondentId(@NotBlank String questionId, @NotNull String respondentId);
}
