package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.Select;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.TimeZone;

@Serdeable
public record ProfileUpdate(@NotNull @Select(fetcher = TimeZoneFetcher.class) TimeZone timeZone,
                            @NotNull DayOfWeek firstDayOfWeek,
                            TimeFormat timeFormat) {
}
