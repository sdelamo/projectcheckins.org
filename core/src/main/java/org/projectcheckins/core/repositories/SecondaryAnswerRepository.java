package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.Generated;
import org.projectcheckins.core.api.Answer;

import java.util.List;
import java.util.Optional;


@Generated // "ignore for jacoco"
@Requires(env = Environment.TEST)
@Secondary
@Singleton
public class SecondaryAnswerRepository implements AnswerRepository {
    @Override
    @NonNull
    public String save(@NotNull @Valid Answer answer,
                       @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @NonNull
    public Optional<? extends Answer> findById(@NotBlank String id, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @NonNull
    public List<? extends Answer> findByQuestionId(@NotBlank String questionId,
                                                   @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<? extends Answer> findByQuestionIdAndRespondentId(String questionId, String respondentId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
