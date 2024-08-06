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
import io.micronaut.context.event.ApplicationEventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Requires(beans = {EmailInvitationSender.class})
@Singleton
class InvitationSavedEventListener implements ApplicationEventListener<InvitationSavedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(InvitationSavedEventListener.class);

    private final EmailInvitationSender emailInvitationSender;

    InvitationSavedEventListener(EmailInvitationSender emailInvitationSender) {
        this.emailInvitationSender = emailInvitationSender;
    }

    @Override
    public void onApplicationEvent(InvitationSavedEvent event) {
        LOG.trace("Received InvitationSavedEvent with email {} and url {} - {}", event.getEmail(), event.getUrl(), event.getSource());
        emailInvitationSender.sendInvitationEmail(event.getEmail(), event.getUrl(), event.getLocale());
    }
}
