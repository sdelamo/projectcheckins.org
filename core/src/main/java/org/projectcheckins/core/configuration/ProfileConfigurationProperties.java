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

package org.projectcheckins.core.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.TimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

@ConfigurationProperties("projectcheckins.profile")
public class ProfileConfigurationProperties implements ProfileConfiguration {
    private static final DayOfWeek DEFAULT_FIRST_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final LocalTime DEFAULT_BEGINNING_OF_DAY = LocalTime.of(9, 0);
    private static final LocalTime DEFAULT_END_OF_DAY = LocalTime.of(16, 30);
    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
    private static final TimeFormat DEFAULT_TIME_FORMAT = TimeFormat.TWENTY_FOUR_HOUR_CLOCK;
    private static final Format DEFAULT_FORMAT = Format.MARKDOWN;
    private DayOfWeek firstDayOfWeek = DEFAULT_FIRST_DAY_OF_WEEK;
    private LocalTime beginningOfDay = DEFAULT_BEGINNING_OF_DAY;
    private LocalTime endOfDay = DEFAULT_END_OF_DAY;
    private TimeZone timeZone = DEFAULT_TIME_ZONE;
    private TimeFormat timeFormat = DEFAULT_TIME_FORMAT;
    private Format format = DEFAULT_FORMAT;

    @Override
    public DayOfWeek getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    @Override
    public LocalTime getBeginningOfDay() {
        return beginningOfDay;
    }

    @Override
    public LocalTime getEndOfDay() {
        return endOfDay;
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public void setBeginningOfDay(LocalTime beginningOfDay) {
        this.beginningOfDay = beginningOfDay;
    }

    public void setEndOfDay(LocalTime endOfDay) {
        this.endOfDay = endOfDay;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setTimeFormat(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
    }

    @Override
    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
