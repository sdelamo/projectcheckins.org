package org.projectcheckins.security;

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
@Property(name = "spec.name", value = "EmailInvitationSenderTest")
@Property(name = "email.sender", value = "info@projectcheckins.org")
class EmailInvitationSenderTest {

    @Test
    void testSendInvitationEmail(EmailInvitationSender emailInvitationSender, EmailSenderReplacement<?> emailSenderReplacement) {
        final String recipient = "delamos@unityfoundation.io";
        emailInvitationSender.sendInvitationEmail(recipient, "http://example.com/signUp", Locale.ENGLISH);

        await().atMost(1, SECONDS).until(() -> !emailSenderReplacement.getEmails().isEmpty());

        assertThat(emailSenderReplacement.getEmails())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("subject", "You have been invited to join a Project Check-ins team")
                .hasFieldOrPropertyWithValue("from", new Contact("info@projectcheckins.org"))
                .hasFieldOrPropertyWithValue("to", List.of(new Contact(recipient)))
                .extracting(Email::getBody)
                .isInstanceOf(MultipartBody.class)
                .satisfies(body -> assertThat(body.get(BodyType.TEXT)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and signup:
                http://example.com/signUp""")))
                .satisfies(body -> assertThat(body.get(BodyType.HTML)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and signup:
                <a href="http://example.com/signUp""")));

        emailSenderReplacement.getEmails().clear();
    }

    @Requires(property = "spec.name", value = "EmailInvitationSenderTest")
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
