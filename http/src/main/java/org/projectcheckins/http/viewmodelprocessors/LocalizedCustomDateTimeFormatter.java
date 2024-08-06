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

package org.projectcheckins.http.viewmodelprocessors;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class LocalizedCustomDateTimeFormatter implements CustomDateTimeFormatter {
    private final Locale locale;
    private final MessageSource messageSource;
    private final static String PATTERN = "EEEE, MMMM d";
    private final static String PATTERN_WITH_YEAR = "EEEE, MMMM d yyyy";
    private final DateTimeFormatter fomatter;
    private final DateTimeFormatter formatterWithYear;

    public LocalizedCustomDateTimeFormatter(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.fomatter = DateTimeFormatter.ofPattern(PATTERN, locale);
        this.formatterWithYear = DateTimeFormatter.ofPattern(PATTERN_WITH_YEAR, locale);
    }

    @Override
    @NonNull
    public String format(@NonNull @NotNull LocalDate dateTime) {
        if (dateTime == null) {
            return "";
        }
        if (dateTime.equals(LocalDate.now().minusDays(1))) {
            return messageSource.getMessage("yesterday", "Yesterday", locale);
        } else if (dateTime.equals(LocalDate.now())) {
            return messageSource.getMessage("today", "Today", locale);
        }
        return dateTime.getYear() == LocalDate.now().getYear() ? dateTime.format(fomatter) : dateTime.format(formatterWithYear);
    }
}
