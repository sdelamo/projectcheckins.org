package org.projectcheckins.http.i18n;

import io.micronaut.context.MessageSource;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class MessageSourceFactoryTest {
    @Test
    void messageSource(MessageSource messageSource) {
        Optional<String> message = messageSource.getMessage("title.notFound", Locale.ENGLISH);
        assertTrue(message.isPresent());
        assertEquals("Not Found", message.get());
    }

}