// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
@Property(name = "spec.name", value = "EmailResetPasswordSenderTest")
@Property(name = "email.sender", value = "info@projectcheckins.org")
class EmailResetPasswordSenderTest {

    @Test
    void testSendResetPasswordEmail(EmailResetPasswordSender emailResetPasswordSender, EmailSenderReplacement<?> emailSenderReplacement) {
        final String recipient = "delamos@unityfoundation.io";
        emailResetPasswordSender.sendResetPasswordEmail(recipient, "http://example.com/resetPassword?token=foo", Locale.ENGLISH);

        await().atMost(1, SECONDS).until(() -> !emailSenderReplacement.getEmails().isEmpty());

        assertThat(emailSenderReplacement.getEmails())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("subject", "Reset your password")
                .hasFieldOrPropertyWithValue("from", new Contact("info@projectcheckins.org"))
                .hasFieldOrPropertyWithValue("to", List.of(new Contact(recipient)))
                .extracting(Email::getBody)
                .isInstanceOf(MultipartBody.class)
                .satisfies(body -> assertThat(body.get(BodyType.TEXT)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and reset your password:
                http://example.com/resetPassword?token=foo""")))
                .satisfies(body -> assertThat(body.get(BodyType.HTML)).hasValueSatisfying(x -> assertThat(x).startsWith("""
                Please, click the link below and reset your password:
                <a href="http://example.com/resetPassword?token=foo""")));

        emailSenderReplacement.getEmails().clear();
    }

    @Requires(property = "spec.name", value = "EmailResetPasswordSenderTest")
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
