package org.projectcheckins.security.i18n;

import io.micronaut.context.MessageSource;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class SecurityMessageSourceFactoryTest {

    @Test
    void securityMessageSource(MessageSource messageSource) {
        Optional<String> message = messageSource.getMessage("nav.signup", Locale.ENGLISH);
        assertTrue(message.isPresent());
        assertEquals("Sign Up", message.get());
    }
}