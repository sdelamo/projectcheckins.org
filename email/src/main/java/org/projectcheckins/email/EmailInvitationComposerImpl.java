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
class EmailInvitationComposerImpl implements EmailInvitationComposer {

    private final EmailConfiguration emailConfiguration;
    private final MessageSource messageSource;

    EmailInvitationComposerImpl(EmailConfiguration emailConfiguration, MessageSource messageSource) {
        this.emailConfiguration = emailConfiguration;
        this.messageSource = messageSource;
    }

    @Override
    @NonNull
    public Email.Builder composeEmailInvitation(@NonNull @NotNull Locale locale,
                                                @NonNull @NotNull @Valid EmailInvitation emailInvitation) {
        String subject = messageSource.getMessage("email.invitation.subject", "You have been invited to join a Project Check-ins team", locale);
        String callToAction = messageSource.getMessage("email.invitation.calltoaction", "Please, click the link below and signup:", locale);
        String callToActionLink = messageSource.getMessage("email.invitation.calltoaction.link", "Sign up on Project Check-ins", locale);
        String url = emailInvitation.url();
        String text = String.join("\n", callToAction, url);
        String html = String.join("\n", callToAction, "<a href=\"" + url + "\">" + callToActionLink + "</a>");
        return Email.builder()
                .from(emailConfiguration.getSender())
                .to(emailInvitation.email())
                .subject(subject)
                .body(html, text);
    }
}
