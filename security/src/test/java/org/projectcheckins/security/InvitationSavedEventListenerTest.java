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
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.forms.TeamMemberSave;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Property(name = "spec.name", value = "InvitationSavedEventListenerTest")
@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest(startApplication = false)
class InvitationSavedEventListenerTest {

    @Inject
    ApplicationEventPublisher<InvitationSavedEvent> publisher;

    @Inject
    EmailInvitationSenderMock emailSender;

    @Test
    void testOnApplicationEvent() {
        final String recipient = "delamos@unityfoundation.io";
        final String url = "http://example.com/signUp";
        publisher.publishEvent(new InvitationSavedEvent(new TeamMemberSave(recipient), Locale.ENGLISH, url));

        await().atMost(3, SECONDS).until(() -> !emailSender.emails.isEmpty());

        assertThat(emailSender.emails)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("email", recipient)
                .hasFieldOrPropertyWithValue("url", url);

        emailSender.emails.clear();
    }

    @Requires(property = "spec.name", value = "InvitationSavedEventListenerTest")
    @Singleton
    @Replaces(EmailInvitationSenderImpl.class)
    static class EmailInvitationSenderMock implements EmailInvitationSender {
        final List<Map<String, String>> emails = new ArrayList<>();
        @Override
        public void sendInvitationEmail(String email, String url, Locale locale) {
            emails.add(Map.of("email", email, "url", url));
        }
    }
}
