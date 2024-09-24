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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.util.Locale;

@Requires(beans = EmailConfiguration.class)
@Singleton
class EmailConfirmationComposerImpl implements EmailConfirmationComposer {

    private final MessageSource messageSource;
    private final EmailConfiguration emailConfiguration;
    private final EmailConfirmationTokenGenerator emailConfirmationTokenGenerator;

    EmailConfirmationComposerImpl(MessageSource messageSource,
                                  EmailConfiguration emailConfiguration, EmailConfirmationTokenGenerator emailConfirmationTokenGenerator) {
        this.messageSource = messageSource;
        this.emailConfiguration = emailConfiguration;
        this.emailConfirmationTokenGenerator = emailConfirmationTokenGenerator;
    }

    @Override
    @NonNull
    public Email.Builder composeEmailConfirmation(@NonNull @NotNull Locale locale,
                                           @NonNull @NotNull @Valid EmailConfirmation emailConfirmation) {

        String subject = messageSource.getMessage("email.confirm.subject", "Confirm email address for your Project Check-ins account", locale);
        String callToAction = messageSource.getMessage("email.confirm.calltoaction", "To confirm your email address, please click the link below:", locale);
        String callToActionLink = messageSource.getMessage("email.confirm.calltoaction.link", "Confirm email", locale);
        String token = emailConfirmationTokenGenerator.generateToken(emailConfirmation.email());
        String url = emailConfirmation.url() + "?token=" + token;
        String text = String.join("\n", callToAction, url);
        String html = String.join("\n", callToAction, "<a href=\"" + url + "\">" + callToActionLink + "</a>");
        return Email.builder()
                .from(emailConfiguration.getSender())
                .to(emailConfirmation.email())
                .subject(subject)
                .body(html, text);
    }
}
