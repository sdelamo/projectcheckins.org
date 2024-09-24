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

package org.projectcheckins.email.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.locale.LocaleResolutionConfiguration;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.EmailSender;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.event.LoginFailedEvent;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.email.EmailConfirmationComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

@Property(name = "spec.name", value = "LoginFailedEventListenerTest")
@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest(startApplication = false)
class LoginFailedEventListenerTest {

    @Test
    void loginFailedForDisabledSendeEmailConfirmation(ApplicationEventPublisher<LoginFailedEvent> loginFailedEventPublisher,
                                                      EmailSenderReplacement<?> emailSenderReplacement) {

        String recipient = "delamos@unityfoundation.io";
        AuthenticationResponse authenticationResponse = AuthenticationResponse.failure(AuthenticationFailureReason.USER_DISABLED);
        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(recipient, "password");
        String host = "https://projectcheckins.example.com";
        loginFailedEventPublisher.publishEvent(new LoginFailedEvent(authenticationResponse, usernamePasswordCredentials, host, Locale.ENGLISH));
        await().atMost(3, SECONDS).until(() -> !emailSenderReplacement.getEmails().isEmpty());
        List<Email> emails = emailSenderReplacement.getEmails();
        assertThat(emails).hasSize(1);
        Email email = emails.getFirst();
        AssertConfirmationEmailUtils.assertConfirmationEmail(recipient, email);
        emailSenderReplacement.getEmails().clear();
    }

    @Requires(property = "spec.name", value = "LoginFailedEventListenerTest")
    @Singleton
    @Replaces(EmailConfirmationSender.class)
    static class EmailConfirmationSenderImplReplacement extends EmailConfirmationSenderImpl {

        EmailConfirmationSenderImplReplacement(EmailConfirmationComposer emailConfirmationComposer,
                                               EmailSender<?, ?> emailSender,
                                               EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration) {
            super(emailConfirmationComposer, emailSender, emailConfirmationControllerConfiguration);
        }
    }
    @Requires(property = "spec.name", value = "LoginFailedEventListenerTest")
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