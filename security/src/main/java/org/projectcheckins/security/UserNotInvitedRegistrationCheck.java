package org.projectcheckins.security;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.views.fields.messages.Message;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@Singleton
public class UserNotInvitedRegistrationCheck implements RegistrationCheck {
    private static final Message MESSAGE_USER_NOT_INVITED = new Message("User not invited", "user.not.invited");
    private static final RegistrationCheckViolation VIOLATION_USER_NOT_INVITED = new RegistrationCheckViolation(MESSAGE_USER_NOT_INVITED);
    private final TeamInvitationRepository teamInvitationRepository;

    public UserNotInvitedRegistrationCheck(TeamInvitationRepository teamInvitationRepository) {
        this.teamInvitationRepository = teamInvitationRepository;
    }

    @Override
    public Optional<RegistrationCheckViolation> validate(@NotBlank @Email String email, @Nullable Tenant tenant) {
        return teamInvitationRepository.existsByEmail(email, tenant)
                ? Optional.empty()
                : Optional.of(VIOLATION_USER_NOT_INVITED);
    }
}
