package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.fields.Form;
import jakarta.validation.constraints.NotNull;

import java.util.function.Function;

@FunctionalInterface
public interface AnswerSaveFormGenerator {
    @NonNull
    Form generate(@NotNull Function<Format, String> actionFunction, @NotNull Authentication authentication);
}
