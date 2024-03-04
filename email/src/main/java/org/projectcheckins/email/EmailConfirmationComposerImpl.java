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
