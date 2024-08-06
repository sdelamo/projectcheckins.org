// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.api.Respondent;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@NotEmptyDays
@NotEmptyRespondents
@Serdeable
public record QuestionFormRecord(@NotBlank String title,
                                 @NotNull HowOften howOften,
                                 @NotNull TimeOfDay timeOfDay,
                                 @NotNull LocalTime fixedTime,
                                 @Nullable Set<DayOfWeek> dailyOnDay,
                                 @Nullable DayOfWeek onceAWeekDay,
                                 @Nullable DayOfWeek everyOtherWeekDay,
                                 @Nullable DayOfWeek onceAMonthOnTheFirstDay,
                                 @Nullable Set<String> respondentIds,
                                 @Nullable Map<String, List<Message>> fieldErrors,
                                 @Nullable List<Message> errors) implements QuestionForm {

    @NonNull
    public static QuestionFormRecord of(@NonNull QuestionForm form, @NonNull ConstraintViolationException ex) {
        ValidatedFieldset errors = ConstraintViolationExceptionUtils.validatedFieldset(ex, QuestionFormRecord.class);
        return new QuestionFormRecord(
                form.title(),
                form.howOften(),
                form.timeOfDay(),
                form.fixedTime(),
                form.days(),
                form.onceAWeekDay(),
                form.everyOtherWeekDay(),
                form.onceAMonthOnTheFirstDay(),
                form.respondentIds(),
                errors.fieldErrors(),
                errors.errors());
    }

    @NonNull
    public static QuestionFormRecord of(@NonNull Question question) {
        return new QuestionFormRecord(
                question.title(),
                question.howOften(),
                question.timeOfDay(),
                question.fixedTime(),
                question.howOften() == HowOften.DAILY_ON ? question.days() : Collections.singleton(DayOfWeek.MONDAY),
                question.howOften() == HowOften.ONCE_A_WEEK ? question.days().stream().findFirst().orElseThrow() : DayOfWeek.MONDAY,
                question.howOften() == HowOften.EVERY_OTHER_WEEK ? question.days().stream().findFirst().orElseThrow() : DayOfWeek.MONDAY,
                question.howOften() == HowOften.ONCE_A_MONTH_ON_THE_FIRST ? question.days().stream().findFirst().orElseThrow() : DayOfWeek.MONDAY,
                question.respondents().stream().map(Respondent::id).collect(Collectors.toSet()),
                null,
                null
        );
    }
}
