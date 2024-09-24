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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

@NotEmptyDays
@NotEmptyRespondents
public interface QuestionForm extends ValidatedFieldset {
    @NotBlank String title();
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
}
