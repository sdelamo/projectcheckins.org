package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Question;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Serdeable
public record QuestionRecord(
        @NotBlank String id,
        @NotBlank String title,
        @NotNull HowOften howOften,
        @NotEmpty Set<DayOfWeek> days,
        @NotNull TimeOfDay timeOfDay,
        @NotNull LocalTime fixedTime
) implements Question {
}
