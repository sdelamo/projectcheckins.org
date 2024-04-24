package org.projectcheckins.netty.gmail;

import io.micronaut.context.annotation.Requires;
import io.micronaut.email.javamail.sender.MailPropertiesProvider;
import io.micronaut.email.javamail.sender.SessionProvider;
import jakarta.inject.Singleton;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.projectcheckins.email.EmailConfiguration;

@Requires(beans = { EmailConfiguration.class, GmailConfiguration.class})
@Singleton
class GmailSessionProvider implements SessionProvider {

    private final MailPropertiesProvider mailPropertiesProvider;
    private final EmailConfiguration emailConfiguration;
    private final GmailConfiguration gmailConfiguration;

    GmailSessionProvider(GmailConfiguration gmailConfiguration,
                         GmailMailPropertiesProvider mailPropertiesProvider,
                         EmailConfiguration emailConfiguration) {
        this.mailPropertiesProvider = mailPropertiesProvider;
        this.gmailConfiguration = gmailConfiguration;
        this.emailConfiguration = emailConfiguration;
    }

    @Override
    public Session session() {
        return Session.getInstance(mailPropertiesProvider.mailProperties(),
                new jakarta.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailConfiguration.getSender(), gmailConfiguration.getAppSpecificPassword());
                    }
                });
    }
}
