package org.projectcheckins.core.models;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.api.AnswerView;

import java.time.LocalDate;
import java.util.List;

@Serdeable
public record DateAnswers(@NonNull @NotNull @PastOrPresent LocalDate answerDate,
                          @NonNull @NotEmpty List<? extends AnswerView> answers) {
}
