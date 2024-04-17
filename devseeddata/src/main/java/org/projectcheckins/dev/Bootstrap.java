package org.projectcheckins.dev;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;

import io.micronaut.multitenancy.Tenant;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.RegistrationCheckViolationException;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.annotations.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated// "ignore for jacoco"
@Requires(env = Environment.DEVELOPMENT)
@Singleton
public class Bootstrap implements ApplicationEventListener<ServerStartupEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    private final RegisterService registerService;
    private final TeamInvitationRepository teamInvitationRepository;
    private final EmailConfirmationRepository emailConfirmationRepository;

    public Bootstrap(RegisterService registerService,
                     TeamInvitationRepository teamInvitationRepository,
                     EmailConfirmationRepository emailConfirmationRepository) {
        this.registerService = registerService;
        this.teamInvitationRepository = teamInvitationRepository;
        this.emailConfirmationRepository = emailConfirmationRepository;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        Tenant tenant = null;
        teamInvitationRepository.save(new TeamInvitationRecord("pending@example.com", tenant));
        addUser("delamos@unityfoundation.io", tenant);
        addUser("calvog@unityfoundation.io", tenant);
        addUser("grellej@unityfoundation.io", tenant);
        addUser("yatest@unityfoundation.io", tenant);
        addUser("wetted@objectcomputing.com", tenant);
    }

    private void addUser(String email, Tenant tenant) {
        try {
            teamInvitationRepository.save(new TeamInvitationRecord(email, tenant));
            registerService.register(email, "secret", tenant);
        } catch (RegistrationCheckViolationException e) {
            LOG.warn("{}", e.getViolation().message().defaultMessage());
        }
        emailConfirmationRepository.enableByEmail(email);
    }
}