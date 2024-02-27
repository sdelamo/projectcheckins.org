package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    @NonNull
    String save(@NotNull @Valid QuestionSave questionSave);

    @NonNull
    Optional<Question> findById(@NotBlank String id);

    void update(@NotNull @Valid QuestionUpdate questionUpdate);

    @NonNull
    List<Question> findAll();

    void deleteById(@NotBlank String id);
}
