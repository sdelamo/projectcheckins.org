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

package org.projectcheckins.notification.logger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.TimeZone;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest
class LoggerNotifierTest {

    @Test
    void notify(LoggerNotifier notifier) {
        final Question question = new QuestionRecord("id", "title", HowOften.DAILY_ON,
                Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                TimeOfDay.END, LocalTime.of(16, 30), Set.of(new RespondentRecord("id", now())));
        final Profile profile = new ProfileRecord("id", "email@example.com", TimeZone.getDefault(), DayOfWeek.MONDAY, LocalTime.of(9, 9), LocalTime.of(16, 0), TimeFormat.TWENTY_FOUR_HOUR_CLOCK, Format.MARKDOWN, "Guillermo", "Calvo", true);
        final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        ((Logger) LoggerFactory.getLogger(LoggerNotifier.class)).addAppender(listAppender);
        listAppender.start();
        assertThatCode(() -> notifier.notify(question, profile))
                .doesNotThrowAnyException();
        assertThat(listAppender.list).anySatisfy(e -> assertThat(e)
                .hasToString("[INFO] Asking user: Guillermo Calvo question: title"));
    }
}
