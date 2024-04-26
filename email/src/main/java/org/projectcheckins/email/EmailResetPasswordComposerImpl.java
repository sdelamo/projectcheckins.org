package org.projectcheckins.email;

import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;

@Requires(beans = EmailConfiguration.class)
@Singleton
class EmailResetPasswordComposerImpl implements EmailResetPasswordComposer {

    private final EmailConfiguration emailConfiguration;
    private final MessageSource messageSource;

    EmailResetPasswordComposerImpl(EmailConfiguration emailConfiguration, MessageSource messageSource) {
        this.emailConfiguration = emailConfiguration;
        this.messageSource = messageSource;
    }

    @Override
    @NonNull
    public Email.Builder composeEmailResetPassword(@NonNull @NotNull Locale locale,
                                                   @NonNull @NotNull @Valid EmailResetPassword emailResetPassword) {
        String subject = messageSource.getMessage("email.resetPassword.subject", "Reset your password", locale);
        String callToAction = messageSource.getMessage("email.resetPassword.calltoaction", "Please, click the link below and reset your password:", locale);
        String callToActionLink = messageSource.getMessage("email.resetPassword.calltoaction.link", "Reset your password on Project Check-ins", locale);
        String url = emailResetPassword.url();
        String text = String.join("\n", callToAction, url);
        String html = String.join("\n", callToAction, "<a href=\"" + url + "\">" + callToActionLink + "</a>");
        return Email.builder()
                .from(emailConfiguration.getSender())
                .to(emailResetPassword.email())
                .subject(subject)
                .body(html, text);
    }
}
