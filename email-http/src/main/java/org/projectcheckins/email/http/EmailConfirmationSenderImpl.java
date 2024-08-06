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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.locale.LocaleResolutionConfiguration;
import io.micronaut.email.EmailSender;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.email.EmailConfirmation;
import org.projectcheckins.email.EmailConfirmationComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Optional;

@Requires(beans = {EmailConfirmationComposer.class, EmailSender.class})
@Singleton
class EmailConfirmationSenderImpl implements EmailConfirmationSender {
    private static final Logger LOG = LoggerFactory.getLogger(EmailConfirmationSenderImpl.class);
    private final EmailConfirmationComposer emailConfirmationComposer;
    private final EmailSender<?, ?> emailSender;
    private final EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration;

    EmailConfirmationSenderImpl(EmailConfirmationComposer emailConfirmationComposer,
                                EmailSender<?, ?> emailSender,
                                EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration) {
        this.emailConfirmationComposer = emailConfirmationComposer;
        this.emailSender = emailSender;
        this.emailConfirmationControllerConfiguration = emailConfirmationControllerConfiguration;
    }

    @Override
    public void sendConfirmationEmail(@NonNull @NotBlank @Email String email,
                                      @NonNull @NotBlank String host,
                                      @NonNull @NotNull Locale locale) {
        String url = host + emailConfirmationControllerConfiguration.getPath();
        LOG.trace("Sending email confirmation to {} with url {}", email, url);
        sendEmail(email, url, locale);
    }

    @Async
    protected void sendEmail(@NonNull String email,
                             @NonNull String url,
                             @NonNull Locale locale) {
        EmailConfirmation emailConfirmation = new EmailConfirmation(url, email);
        io.micronaut.email.Email.Builder emailBuilder = emailConfirmationComposer.composeEmailConfirmation(locale, emailConfirmation);
        emailSender.send(emailBuilder);
    }
}
