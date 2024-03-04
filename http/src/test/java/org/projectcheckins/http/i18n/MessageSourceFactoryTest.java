package org.projectcheckins.http.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.context.MessageSource;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Locale;

@MicronautTest(startApplication = false)
class MessageSourceFactoryTest {
    @Test
    void messageSource(MessageSource messageSource) {
        assertThat(messageSource.getMessage("title.notFound", Locale.ENGLISH))
            .contains("Not Found");
    }
}