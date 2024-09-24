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
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.event.LoginFailedEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Requires(beans = {EmailConfirmationSender.class})
@Singleton
class LoginFailedEventListener implements ApplicationEventListener<LoginFailedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(LoginFailedEventListener.class);

    private final EmailConfirmationSender emailConfirmationSender;

    LoginFailedEventListener(EmailConfirmationSender emailConfirmationSender) {
        this.emailConfirmationSender = emailConfirmationSender;
    }

    @Override
    public void onApplicationEvent(LoginFailedEvent event) {

        if (event.getSource() instanceof AuthenticationFailed authenticationFailed) {
            LOG.trace("Logging failed with email  {} reason {}", event.getAuthenticationRequest().getIdentity(), authenticationFailed.getReason());
            if (authenticationFailed.getReason() == AuthenticationFailureReason.USER_DISABLED) {
                String email = event.getAuthenticationRequest().getIdentity().toString();
                emailConfirmationSender.sendConfirmationEmail(email, event.getHost().orElse(""), event.getLocale());
            }
        }
    }
}