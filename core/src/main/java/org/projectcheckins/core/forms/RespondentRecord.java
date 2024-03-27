package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.core.api.Respondent;

@Serdeable
public record RespondentRecord(
        @NotBlank String id
) implements Respondent {
}
