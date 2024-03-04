package org.projectcheckins.email.http;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.email.EmailConfirmationTokenValidator;

import java.net.URI;
import java.util.Optional;

@Controller("${" + EmailConfirmationControllerConfigurationProperties.PREFIX + ".path:"+ EmailConfirmationControllerConfigurationProperties.DEFAULT_PATH +"}")
class EmailConfirmationController {

    private final EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration;
    private final EmailConfirmationTokenValidator emailConfirmationTokenValidator;
    private final EmailConfirmationRepository emailConfirmationRepository;

    EmailConfirmationController(EmailConfirmationControllerConfiguration emailConfirmationControllerConfiguration,
                                EmailConfirmationTokenValidator emailConfirmationTokenValidator,
                                EmailConfirmationRepository emailConfirmationRepository) {
        this.emailConfirmationControllerConfiguration = emailConfirmationControllerConfiguration;
        this.emailConfirmationTokenValidator = emailConfirmationTokenValidator;
        this.emailConfirmationRepository = emailConfirmationRepository;
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get
    HttpResponse<?> confirm(@QueryValue String token) {
        Optional<Authentication> authenticationOptional = emailConfirmationTokenValidator.validate(token);
        if (authenticationOptional.isEmpty()) {
            return HttpResponse.seeOther(URI.create(emailConfirmationControllerConfiguration.getFailureRedirect()));
        }
        Authentication authentication = authenticationOptional.get();
        emailConfirmationRepository.enableByEmail(authentication.getName());
        return HttpResponse.seeOther(URI.create(emailConfirmationControllerConfiguration.getSuccessfulRedirect()));
    }
}
