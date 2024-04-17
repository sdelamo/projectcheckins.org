package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRegisterService implements RegisterService, PasswordService {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRegisterService.class);

    private final PasswordEncoder passwordEncoder;
    private final List<RegistrationCheck> registrationChecks;
    private final TeamInvitationRepository teamInvitationRepository;

    protected AbstractRegisterService(
            PasswordEncoder passwordEncoder,
            List<RegistrationCheck> registrationChecks,
            TeamInvitationRepository teamInvitationRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.registrationChecks = registrationChecks;
        this.teamInvitationRepository = teamInvitationRepository;
    }

    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword,
                           @Nullable Tenant tenant) throws RegistrationCheckViolationException {
        return register(username, rawPassword, tenant, Collections.emptyList());
    }

    @NonNull
    protected abstract String register(@NonNull @NotNull @Valid UserSave userSave, @Nullable Tenant tenant) throws RegistrationCheckViolationException;

    @Override
    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword,
                           @Nullable Tenant tenant,
                           @NonNull List<String> authorities) throws RegistrationCheckViolationException {
        Optional<RegistrationCheckViolation> violationOptional = registrationChecks.stream()
                .map(check -> check.validate(username, tenant))
                .flatMap(Optional::stream)
                .findFirst();
        if (violationOptional.isPresent()) {
            LOG.warn("Could not register {}", violationOptional.get().message().defaultMessage());
            throw new RegistrationCheckViolationException(violationOptional.get());
        }
        final String encodedPassword = passwordEncoder.encode(rawPassword);
        final String id = register(new UserSave(username, encodedPassword, authorities), tenant);
        teamInvitationRepository.deleteByEmail(username, tenant);
        return id;
    }

    @Override
    public void updatePassword(@NonNull @NotBlank String userId,
                               @NonNull @NotBlank String newRawPassword) {
        final String newEncodedPassword = passwordEncoder.encode(newRawPassword);
        updatePassword(new PasswordUpdate(userId, newEncodedPassword));
    }

    @NonNull
    protected abstract void updatePassword(@NonNull @NotNull @Valid PasswordUpdate passwordUpdate);
}
