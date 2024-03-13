package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Profile;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

@Serdeable
public record ProfileRecord(
        @NonNull @NotBlank @Email String email,
        @NotNull TimeZone timeZone,
        @NotNull DayOfWeek firstDayOfWeek,
        @NotNull LocalTime beginningOfDay,
        @NotNull LocalTime endOfDay,
        @NotNull TimeFormat timeFormat,
        @NotNull Format format,
        @Nullable String firstName,
        @Nullable String lastName
) implements Profile {
}
