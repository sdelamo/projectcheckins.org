package org.projectcheckins.security;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Property(name = "spec.name", value = "PasswordResetEventListenerTest")
@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest(startApplication = false)
class PasswordResetEventListenerTest {

    @Inject
    ApplicationEventPublisher<PasswordResetEvent> publisher;

    @Inject
    EmailResetPasswordSenderMock emailSender;

    @Test
    void testOnApplicationEvent() {
        final String recipient = "delamos@unityfoundation.io";
        final String url = "http://example.com/passwordReset?token=foo";
        publisher.publishEvent(new PasswordResetEvent(recipient, Locale.ENGLISH, url));

        await().atMost(3, SECONDS).until(() -> !emailSender.emails.isEmpty());

        assertThat(emailSender.emails)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("email", recipient)
                .hasFieldOrPropertyWithValue("url", url);

        emailSender.emails.clear();
    }

    @Requires(property = "spec.name", value = "PasswordResetEventListenerTest")
    @Singleton
    @Replaces(EmailResetPasswordSenderImpl.class)
    static class EmailResetPasswordSenderMock implements EmailResetPasswordSender {
        final List<Map<String, String>> emails = new ArrayList<>();
        @Override
        public void sendResetPasswordEmail(String email, String url, Locale locale) {
            emails.add(Map.of("email", email, "url", url));
        }
    }
}
