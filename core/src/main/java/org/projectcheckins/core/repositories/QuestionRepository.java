package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.Saved;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    @NonNull
    String save(@NotNull @Valid Question questionSave, @Nullable Tenant tenant);

    @NonNull
    Optional<? extends Question> findById(@NotBlank String id, @Nullable Tenant tenant);

    @Validated(groups = Saved.class)
    void update(@NotNull @Valid Question questionUpdate, @Nullable Tenant tenant);

    @NonNull
    List<? extends Question> findAll(@Nullable Tenant tenant);

    void deleteById(@NotBlank String id, @Nullable Tenant tenant);

    @NonNull
    default String save(@NotNull @Valid Question questionSave) {
        return save(questionSave, null);
    }

    @NonNull
    default Optional<? extends Question> findById(@NotBlank String id) {
        return findById(id, null);
    }

    @Validated(groups = Saved.class)
    default void update(@NotNull @Valid Question questionUpdate) {
        update(questionUpdate, null);
    }

    @NonNull
    default List<? extends Question> findAll() {
        return findAll(null);
    }

    default void deleteById(@NotBlank String id) {
        deleteById(id, null);
    }
}
