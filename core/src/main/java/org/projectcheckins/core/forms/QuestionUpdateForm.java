package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.QuestionUpdate;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NotEmptyDays
@Serdeable
public record QuestionUpdateForm(@NonNull String id,
                                 @NotBlank String title,
                                 @NotNull HowOften howOften,
                                 @NotNull TimeOfDay timeOfDay,
                                 @NotNull LocalTime fixedTime,
                                 @Nullable Set<DayOfWeek> dailyOnDay,
                                 @Nullable DayOfWeek onceAWeekDay,
                                 @Nullable DayOfWeek everyOtherWeekDay,
                                 @Nullable DayOfWeek onceAMonthOnTheFirstDay,
                                 @Nullable Map<String, List<Message>> fieldErrors,
                                 @Nullable List<Message> errors) implements QuestionUpdate, QuestionForm {

    public QuestionUpdateForm(@NonNull String id,
                              @NotBlank String title,
                              @NotNull HowOften howOften,
                              @NotNull TimeOfDay timeOfDay,
                              @NotNull LocalTime fixedTime,
                              @Nullable Set<DayOfWeek> dailyOnDay,
                              @Nullable DayOfWeek onceAWeekDay,
                              @Nullable DayOfWeek everyOtherWeekDay,
                              @Nullable DayOfWeek onceAMonthOnTheFirstDay) {
        this(id, title, howOften, timeOfDay, fixedTime, dailyOnDay, onceAWeekDay, everyOtherWeekDay, onceAMonthOnTheFirstDay, null, null);
    }

    @NonNull
    public static QuestionUpdateForm of(@NonNull QuestionUpdateForm form, @NonNull ConstraintViolationException ex) {
        ValidatedFieldset errors = ConstraintViolationExceptionUtils.validatedFieldset(ex, QuestionUpdateForm.class);
        return new QuestionUpdateForm(form.id(),
                form.title(),
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
    @NotEmpty
    public Set<DayOfWeek> days() {
        return QuestionForm.super.days();
    }
}
