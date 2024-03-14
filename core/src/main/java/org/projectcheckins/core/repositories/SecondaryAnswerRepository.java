package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.Generated;
import org.projectcheckins.core.forms.AnswerSave;


@Generated // "ignore for jacoco"
@Requires(env = Environment.TEST)
@Secondary
@Singleton
public class SecondaryAnswerRepository implements AnswerRepository {
    @Override
    public void save(@NonNull @NotNull Authentication authentication,
                     @NonNull @NotNull @Valid AnswerSave answerSave) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
