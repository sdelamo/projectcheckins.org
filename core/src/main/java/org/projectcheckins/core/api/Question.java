package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.TimeOfDay;
import org.projectcheckins.core.forms.Saved;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public interface Question {

    @NotBlank(groups = Saved.class) String id();

    @NotBlank String title();

    @NotNull HowOften howOften();
    @NotEmpty Set<DayOfWeek> days();
    @NotNull TimeOfDay timeOfDay();
    @NotNull LocalTime fixedTime();
    @NotEmpty Set<? extends Respondent> respondents();
}
