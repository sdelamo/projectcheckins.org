package org.projectcheckins.email.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.*;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "EmailConfirmationSenderTest")
@Property(name = "email.sender", value = "info@projectcheckins.org")
@Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret", value="pleaseChangeThisSecretForANewOne")
class EmailConfirmationSenderTest {

    @Test
    void testSendConfirmationEmail(EmailConfirmationSender emailConfirmationSender, EmailSenderReplacement<?> emailSenderReplacement) {
        String recipient = "delamos@unityfoundation.io";
        emailConfirmationSender.sendConfirmationEmail(recipient, "https://projectcheckins.example.com", Locale.ENGLISH);
        await().atMost(1, SECONDS).until(() -> !emailSenderReplacement.getEmails().isEmpty());
        List<Email> emails = emailSenderReplacement.getEmails();
        assertThat(emails).hasSize(1);
        Email email = emails.getFirst();
        AssertConfirmationEmailUtils.assertConfirmationEmail(recipient, email);

        emailSenderReplacement.getEmails().clear();
        try {
            emailConfirmationSender.sendConfirmationEmail(recipient);
            Thread.sleep(1000); // waits for 1 second
            assertThat(emailSenderReplacement.getEmails()).isEmpty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



    @Requires(property = "spec.name", value = "EmailConfirmationSenderTest")
    @Singleton
    @Named(EmailSenderReplacement.NAME)
    @Replaces(EmailSender.class)
    static class EmailSenderReplacement<I> implements EmailSender<I, Boolean> {
        private static final String NAME = "EmailSenderReplacement";
        private List<Email> emails = new ArrayList<>();

        @Override
        public @NonNull Boolean send(Email.Builder emailBuilder, @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
            emails.add(emailBuilder.build());
            return Boolean.TRUE;
        }

        @Override
        public @NonNull String getName() {
            return NAME;
        }

        public List<Email> getEmails() {
            return emails;
        }
    }
}