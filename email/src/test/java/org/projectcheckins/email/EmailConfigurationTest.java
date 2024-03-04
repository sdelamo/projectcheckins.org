package org.projectcheckins.email;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest
class EmailConfigurationTest {

    @Test
    void testEmailConfiguration(EmailConfiguration emailConfiguration) {
        assertThat(emailConfiguration.getSender()).isEqualTo("info@projectcheckins.org");
    }
}
