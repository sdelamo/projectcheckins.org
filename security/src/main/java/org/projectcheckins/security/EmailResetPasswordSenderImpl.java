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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email.Builder;
import io.micronaut.email.EmailSender;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.email.EmailInvitationComposer;
import org.projectcheckins.email.EmailResetPassword;
import org.projectcheckins.email.EmailResetPasswordComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


@Requires(beans = {EmailInvitationComposer.class, EmailSender.class})
@Singleton
class EmailResetPasswordSenderImpl implements EmailResetPasswordSender {
    private static final Logger LOG = LoggerFactory.getLogger(EmailResetPasswordSenderImpl.class);
    private final EmailResetPasswordComposer emailResetPasswordComposer;
    private final EmailSender<?, ?> emailSender;

    EmailResetPasswordSenderImpl(EmailResetPasswordComposer emailResetPasswordComposer, EmailSender<?, ?> emailSender) {
        this.emailResetPasswordComposer = emailResetPasswordComposer;
        this.emailSender = emailSender;
    }

    @Override
    public void sendResetPasswordEmail(@NonNull @NotBlank @Email String email,
                                       @NonNull @NotBlank String url,
                                       @NonNull @NotNull Locale locale) {
        LOG.trace("Sending email reset password to {} with url {}", email, url);
        final EmailResetPassword emailResetPassword = new EmailResetPassword(email, url);
        final Builder emailBuilder = emailResetPasswordComposer.composeEmailResetPassword(locale, emailResetPassword);
        emailSender.send(emailBuilder);
    }
}
