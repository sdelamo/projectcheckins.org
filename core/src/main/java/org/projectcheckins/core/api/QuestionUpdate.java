package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public interface QuestionUpdate {
    @NotBlank String id();
    @NotBlank String title();
    @NotNull HowOften howOften();
    @NotEmpty Set<DayOfWeek> days();
    @NotNull TimeOfDay timeOfDay();
    @NotNull LocalTime fixedTime();
    @NotEmpty Set<? extends Respondent> respondents();
}
