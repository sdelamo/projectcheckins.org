package org.projectcheckins.email;

import io.micronaut.context.MessageSource;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
class EmailMessageSourceFactoryTest {
    @Test
    void messageSource(MessageSource messageSource) {
        assertThat(messageSource.getMessage("email.confirm.calltoaction", Locale.ENGLISH))
                .contains("To confirm your email address, please click the link below:");
    }
}

