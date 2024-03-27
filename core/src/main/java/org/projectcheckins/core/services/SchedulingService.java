package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import jakarta.annotation.Nullable;
import org.projectcheckins.core.forms.HowOften;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

public interface SchedulingService {

    @NonNull
    ZonedDateTime nextExecution(@Nullable ZonedDateTime previousDate,
                                @NonNull HowOften frequency,
                                @NonNull Set<DayOfWeek> days,
                                @NonNull ZoneId zoneId,
                                @NonNull LocalTime time);
}
