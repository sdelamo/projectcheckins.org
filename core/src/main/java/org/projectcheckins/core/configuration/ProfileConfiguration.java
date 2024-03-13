package org.projectcheckins.core.configuration;

import io.micronaut.core.annotation.NonNull;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.TimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

public interface ProfileConfiguration {

    @NonNull
    DayOfWeek getFirstDayOfWeek();

    @NonNull
    LocalTime getBeginningOfDay();

    @NonNull
    LocalTime getEndOfDay();

    @NonNull
    TimeZone getTimeZone();

    @NonNull
    TimeFormat getTimeFormat();

    @NonNull
    Format getFormat();
}
