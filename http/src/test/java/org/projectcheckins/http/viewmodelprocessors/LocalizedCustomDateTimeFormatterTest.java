package org.projectcheckins.http.viewmodelprocessors;

import io.micronaut.context.MessageSource;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class LocalizedCustomDateTimeFormatterTest {

    @Test
    void testFormat(MessageSource messageSource) {
        LocalizedCustomDateTimeFormatter formatter = new LocalizedCustomDateTimeFormatter(messageSource, Locale.ENGLISH);
        assertThat(formatter.format(LocalDate.now())).isEqualTo("Today");
        assertThat(formatter.format(LocalDate.now().minusDays(1))).isEqualTo("Yesterday");
        assertThat(formatter.format(LocalDate.of(LocalDate.now().getYear(),4, 7)))
                .isEqualTo(LocalDate.of(LocalDate.now().getYear(),4, 7)
                        .format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)));
        assertThat(formatter.format(LocalDate.of(2023,4, 7))).isEqualTo("Friday, April 7 2023");
    }

}