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

package org.projectcheckins.email;

import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

@Requires(beans = EmailConfiguration.class)
@Singleton
class EmailInvitationComposerImpl implements EmailInvitationComposer {

    private final EmailConfiguration emailConfiguration;
    private final MessageSource messageSource;

    EmailInvitationComposerImpl(EmailConfiguration emailConfiguration, MessageSource messageSource) {
        this.emailConfiguration = emailConfiguration;
        this.messageSource = messageSource;
    }

    @Override
    @NonNull
    public Email.Builder composeEmailInvitation(@NonNull @NotNull Locale locale,
                                                @NonNull @NotNull @Valid EmailInvitation emailInvitation) {
        String subject = messageSource.getMessage("email.invitation.subject", "You have been invited to join a Project Check-ins team", locale);
        String callToAction = messageSource.getMessage("email.invitation.calltoaction", "Please, click the link below and signup:", locale);
        String callToActionLink = messageSource.getMessage("email.invitation.calltoaction.link", "Sign up on Project Check-ins", locale);
        String url = emailInvitation.url();
        String text = String.join("\n", callToAction, url);
        String html = String.join("\n", callToAction, "<a href=\"" + url + "\">" + callToActionLink + "</a>");
        return Email.builder()
                .from(emailConfiguration.getSender())
                .to(emailInvitation.email())
                .subject(subject)
                .body(html, text);
    }
}
