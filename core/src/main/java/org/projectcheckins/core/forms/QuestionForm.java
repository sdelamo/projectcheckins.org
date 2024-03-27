package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Respondent;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public interface QuestionForm extends ValidatedFieldset {
    @NotNull HowOften howOften();
    @NotNull
    TimeOfDay timeOfDay();
    @NotNull
    LocalTime fixedTime();
    @Nullable
    Set<DayOfWeek> dailyOnDay();
    @Nullable
    DayOfWeek onceAWeekDay();
    @Nullable
    DayOfWeek everyOtherWeekDay();
    @Nullable
    DayOfWeek onceAMonthOnTheFirstDay();
    @Nullable
    Set<String> respondentIds();

    @NonNull
    default Set<DayOfWeek> days() {
        if (howOften() == null) {
            return Collections.emptySet();
        }
        return switch (howOften()) {
            case DAILY_ON -> dailyOnDay() != null ? dailyOnDay() : Collections.emptySet();
            case EVERY_OTHER_WEEK -> everyOtherWeekDay() != null ? Collections.singleton(everyOtherWeekDay()) : Collections.emptySet();
            case ONCE_A_WEEK -> onceAWeekDay() != null ? Collections.singleton(onceAWeekDay()) : Collections.emptySet();
            case ONCE_A_MONTH_ON_THE_FIRST -> onceAMonthOnTheFirstDay() != null ? Collections.singleton(onceAMonthOnTheFirstDay()) : Collections.emptySet();
        };
    }

    @NonNull
    default Set<? extends Respondent> respondents() {
        if (respondentIds() == null) {
            return Collections.emptySet();
        }
        return respondentIds().stream().map(RespondentRecord::new).collect(Collectors.toSet());
    }
}
