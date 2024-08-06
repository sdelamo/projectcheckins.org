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

package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import org.projectcheckins.core.forms.HowOften;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.BiFunction;

@Singleton
public class SchedulingServiceImpl implements SchedulingService {

    @Override
    @NonNull
    public ZonedDateTime nextExecution(@Nullable ZonedDateTime previousDate,
                                       @NonNull HowOften frequency,
                                       @NonNull Set<DayOfWeek> days,
                                       @NonNull ZoneId zoneId,
                                       @NonNull LocalTime time) {
        return Optional.ofNullable(previousDate)
                .filter(previous -> isValid(previous, frequency, days))
                .map(previous -> calculateNext(previous, frequency, days))
                .orElseGet(() -> calculateFirst(ZonedDateTime.now(zoneId).with(time), frequency, days))
                .with(time);
    }

    public boolean isValid(@NonNull ZonedDateTime previousDate, @NonNull HowOften frequency, @NonNull Set<DayOfWeek> days) {
        return fallsOn(previousDate, days) && (frequency != HowOften.ONCE_A_MONTH_ON_THE_FIRST || previousDate.getDayOfMonth() <= 7);
    }

    @NonNull
    public ZonedDateTime calculateFirst(@NonNull ZonedDateTime startingDate, @NonNull HowOften frequency, @NonNull Set<DayOfWeek> targetDays) {
        final DayOfWeek targetDay;
        switch (frequency) {
            default:
            case DAILY_ON:
                if (fallsOn(startingDate, targetDays)) {
                    return startingDate;
                }
                return getFirstMatch(startingDate.plusDays(1), SchedulingServiceImpl::fallsOn, targetDays);
            case ONCE_A_WEEK:
            case EVERY_OTHER_WEEK:
                targetDay = getOne(targetDays);
                if (fallsOn(startingDate, targetDay)) {
                    return startingDate;
                }
                return getFirstMatch(startingDate.plusDays(1), SchedulingServiceImpl::fallsOn, targetDay);
            case ONCE_A_MONTH_ON_THE_FIRST:
                targetDay = getOne(targetDays);
                final ZonedDateTime tentative = getFirstMatch(startingDate, SchedulingServiceImpl::fallsOn, targetDay);
                if (tentative.isBefore(startingDate) || tentative.getDayOfMonth() > 7) {
                    return getFirstMatch(startingDate.plusMonths(1).withDayOfMonth(1), SchedulingServiceImpl::fallsOn, targetDay);
                }
                return tentative;
        }
    }

    @NonNull
    public ZonedDateTime calculateNext(@NonNull ZonedDateTime previousDate, @NonNull HowOften frequency, @NonNull Set<DayOfWeek> targetDays) {
        final DayOfWeek targetDay;
        assert isValid(previousDate, frequency, targetDays);
        switch (frequency) {
            default:
            case DAILY_ON:
                targetDay = getNext(previousDate.getDayOfWeek(), targetDays);
                return getFirstMatch(previousDate.plusDays(1), SchedulingServiceImpl::fallsOn, targetDay);
            case ONCE_A_WEEK:
                return previousDate.plusDays(7);
            case EVERY_OTHER_WEEK:
                return previousDate.plusDays(14);
            case ONCE_A_MONTH_ON_THE_FIRST:
                targetDay = getOne(targetDays);
                return getFirstMatch(previousDate.plusMonths(1).withDayOfMonth(1), SchedulingServiceImpl::fallsOn, targetDay);
        }
    }

    private static DayOfWeek getOne(@NonNull Set<DayOfWeek> days) {
        return days.stream().findAny().orElseThrow();
    }

    private static DayOfWeek getNext(@NonNull DayOfWeek previousDay, @NonNull Set<DayOfWeek> days) {
        final List<DayOfWeek> sorted = days.stream().sorted().toList();
        boolean found = false;
        for (DayOfWeek day : sorted) {
            if (found) {
                return day;
            }
            found = (day == previousDay);
        }
        return sorted.getFirst();
    }

    private static boolean fallsOn(@NonNull ZonedDateTime date, @NonNull Set<DayOfWeek> daysOfWeek) {
        return daysOfWeek.contains(date.getDayOfWeek());
    }

    private static boolean fallsOn(@NonNull ZonedDateTime date, @NonNull DayOfWeek dayOfWeek) {
        return dayOfWeek == date.getDayOfWeek();
    }

    private static <T> ZonedDateTime getFirstMatch(@NonNull ZonedDateTime startingDate, @NonNull BiFunction<ZonedDateTime, T, Boolean> predicate, @NonNull T target) {
        ZonedDateTime next = startingDate;
        while (!predicate.apply(next, target)) {
            next = next.plusDays(1);
        }
        return next;
    }
}
