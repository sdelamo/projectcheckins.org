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
