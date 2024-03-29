package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public interface Respondent {

    @NotBlank String id();

    @NotNull ZonedDateTime nextExecution();
}
