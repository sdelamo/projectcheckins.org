package org.projectcheckins.netty.gmail;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.javamail.sender.MailPropertiesProvider;
import jakarta.inject.Singleton;

import java.util.Properties;

@Requires(beans = GmailConfiguration.class)
@Singleton
class GmailMailPropertiesProvider implements MailPropertiesProvider {
    @Override
    public @NonNull Properties mailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        return prop;
    }
}
