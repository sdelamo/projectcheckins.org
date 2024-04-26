package org.projectcheckins.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Requires(beans = {EmailResetPasswordSender.class})
@Singleton
class PasswordResetEventListener implements ApplicationEventListener<PasswordResetEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetEventListener.class);

    private final EmailResetPasswordSender emailResetPasswordSender;

    PasswordResetEventListener(EmailResetPasswordSender emailResetPasswordSender) {
        this.emailResetPasswordSender = emailResetPasswordSender;
    }

    @Override
    public void onApplicationEvent(PasswordResetEvent event) {
        LOG.trace("Received PasswordResetEvent with email {} and url {} - {}", event.getEmail(), event.getUrl(), event.getSource());
        emailResetPasswordSender.sendResetPasswordEmail(event.getEmail(), event.getUrl(), event.getLocale());
    }
}
