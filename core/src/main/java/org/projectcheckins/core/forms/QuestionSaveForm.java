package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.messages.ConstraintViolationUtils;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.QuestionSave;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NotEmptyDays
@Serdeable
public record QuestionSaveForm(@NotBlank String title,
                               @NotNull HowOften howOften,
                               @NotNull TimeOfDay timeOfDay,
                               @NotNull LocalTime fixedTime,
                               @Nullable Set<DayOfWeek> dailyOnDay,
                               @Nullable DayOfWeek onceAWeekDay,
                               @Nullable DayOfWeek everyOtherWeekDay,
                               @Nullable DayOfWeek onceAMonthOnTheFirstDay,
                               @Nullable Map<String, List<Message>> fieldErrors,
                               @Nullable List<Message> errors) implements QuestionSave, QuestionForm {

    @NonNull
    public static QuestionSaveForm of(@NonNull QuestionSaveForm form, @NonNull ConstraintViolationException ex) {
        ValidatedFieldset errors = ConstraintViolationExceptionUtils.validatedFieldset(ex, QuestionSaveForm.class);
        return new QuestionSaveForm(form.title(),
                form.howOften(),
                form.timeOfDay(),
                form.fixedTime(),
                form.dailyOnDay() != null ? form.dailyOnDay : Collections.emptySet(),
                form.onceAWeekDay(),
                form.everyOtherWeekDay(),
                form.onceAMonthOnTheFirstDay(),
                errors.fieldErrors(),
                errors.errors());
    }

    @Override
    public Set<DayOfWeek> days() {
        return QuestionForm.super.days();
    }

    public QuestionSaveForm(@NonNull String title,
                            @NonNull HowOften howOften,
                            @NonNull TimeOfDay timeOfDay,
                            @NotNull LocalTime fixedTime,
                            @Nullable Set<DayOfWeek> dailyOnDay,
                            @Nullable DayOfWeek onceAWeekDay,
                            @Nullable DayOfWeek everyOtherWeekDay,
                            @Nullable DayOfWeek onceAMonthOnTheFirstDay) {
        this(title, howOften, timeOfDay, fixedTime, dailyOnDay, onceAWeekDay, everyOtherWeekDay, onceAMonthOnTheFirstDay, null, null);
    }

    public QuestionSaveForm(String title) {
        this(title, HowOften.DAILY_ON, TimeOfDay.END, LocalTime.of(16, 30), Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), DayOfWeek.MONDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY, null, null);
    }
}
