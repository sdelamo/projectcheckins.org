package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.annotations.Select;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.TimeZone;

@Serdeable
public record ProfileUpdate(@NotNull @Select(fetcher = TimeZoneFetcher.class) TimeZone timeZone,
                            @NotNull DayOfWeek firstDayOfWeek,
                            @NotNull TimeFormat timeFormat,
                            @NotNull Format format,
                            @Nullable String firstName,
                            @Nullable String lastName) {
}
