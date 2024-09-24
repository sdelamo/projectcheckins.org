// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.dev;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;

import io.micronaut.multitenancy.Tenant;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.RegistrationCheckViolationException;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.annotations.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;
import static org.projectcheckins.security.Role.ROLE_ADMIN;

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
        addUser("admin@unityfoundation.io", tenant, ROLE_ADMIN);
        sendInvitation("pending@example.com", tenant);
        addUser("delamos@unityfoundation.io", tenant);
        addUser("calvog@unityfoundation.io", tenant);
        addUser("grellej@unityfoundation.io", tenant);
        addUser("yatest@unityfoundation.io", tenant);
        addUser("wetted@objectcomputing.com", tenant);
    }

    private void addUser(String email, Tenant tenant, String... authorities) {
        try {
            sendInvitation(email, tenant);
            registerService.register(email, "secret", tenant, asList(authorities));
        } catch (RegistrationCheckViolationException e) {
            LOG.warn("{}", e.getViolation().message().defaultMessage());
        }
        emailConfirmationRepository.enableByEmail(email);
    }

    private void sendInvitation(String email, Tenant tenant) {
        try {
            teamInvitationRepository.save(new TeamInvitationRecord(email, tenant));
        } catch (ConstraintViolationException e) {
            LOG.warn("Could not invite {}: {}", email, e.getMessage());
        }
    }
}