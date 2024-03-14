package org.projectcheckins.notification.logger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.QuestionRecord;
import org.projectcheckins.core.forms.TimeFormat;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest
class LoggerNotifierTest {

    @Test
    void notify(LoggerNotifier notifier) {
        final Question question = new QuestionRecord("id", "title", "schedule");
        final Profile profile = new ProfileRecord("email@example.com", TimeZone.getDefault(), DayOfWeek.MONDAY, LocalTime.of(9, 9), LocalTime.of(16, 0), TimeFormat.TWENTY_FOUR_HOUR_CLOCK, Format.MARKDOWN, "Guillermo", "Calvo");
        final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        ((Logger) LoggerFactory.getLogger(LoggerNotifier.class)).addAppender(listAppender);
        listAppender.start();
        assertThatCode(() -> notifier.notify(question, profile))
                .doesNotThrowAnyException();
        assertThat(listAppender.list).anySatisfy(e -> assertThat(e)
                .hasToString("[INFO] Asking user: Guillermo Calvo question: title"));
    }
}
