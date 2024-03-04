package org.projectcheckins.email.http;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.locale.LocaleResolutionConfiguration;
import io.micronaut.email.EmailSender;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.email.EmailConfirmation;
import org.projectcheckins.email.EmailConfirmationComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Optional;


@Requires(beans = {EmailConfirmationComposer.class, EmailSender.class})
@Singleton
class EmailConfirmationSenderImpl implements EmailConfirmationSender {
    private static final Logger LOG = LoggerFactory.getLogger(EmailConfirmationSenderImpl.class);
    private final EmailConfirmationComposer emailConfirmationComposer;
    private final EmailSender<?, ?> emailSender;
    private final HttpHostResolver httpHostResolver;
    private final EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration;
    private final Locale locale;

    EmailConfirmationSenderImpl(EmailConfirmationComposer emailConfirmationComposer,
                                EmailSender<?, ?> emailSender,
                                LocaleResolutionConfiguration localeResolutionConfiguration,
                                HttpHostResolver httpHostResolver,
                                EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration) {
        this.emailConfirmationComposer = emailConfirmationComposer;
        this.emailSender = emailSender;
        this.httpHostResolver = httpHostResolver;
        this.emailConfirmationControllerConfiguration = emailConfirmationControllerConfiguration;
        this.locale = localeResolutionConfiguration.getFixed().orElse(localeResolutionConfiguration.getDefaultLocale());
    }

    @Override
    public void sendConfirmationEmail(@NonNull @NotBlank @Email String email) {
        resolveHost()
                .ifPresentOrElse(host -> sendConfirmationEmail(email, host, locale),
                        () -> LOG.warn("No host found for current request, cannot send confirmation email to {}", email));
    }

    @Override
    public void sendConfirmationEmail(@NonNull @NotBlank @Email String email,
                                      @NonNull @NotBlank String host,
                                      @NonNull @NotNull Locale locale) {
        String url = host + emailConfirmationControllerConfiguration.getPath();
        LOG.trace("Sending email confirmation to {} with url {}", email, url);
        sendEmail(email, url, locale);
    }

    @NonNull
    protected Optional<String> resolveHost() {
        return ServerRequestContext.currentRequest()
                .map(httpHostResolver::resolve);
    }

    @Async
    protected void sendEmail(@NonNull String email,
                             @NonNull String url,
                             @NonNull Locale locale) {
        EmailConfirmation emailConfirmation = new EmailConfirmation(url, email);
        io.micronaut.email.Email.Builder emailBuilder = emailConfirmationComposer.composeEmailConfirmation(locale, emailConfirmation);
        emailSender.send(emailBuilder);
    }
}
