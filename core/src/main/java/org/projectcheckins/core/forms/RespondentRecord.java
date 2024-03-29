package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Respondent;

import java.time.ZonedDateTime;

@Serdeable
public record RespondentRecord(
        @NotBlank String id,
        @NotNull ZonedDateTime nextExecution
) implements Respondent {
}
