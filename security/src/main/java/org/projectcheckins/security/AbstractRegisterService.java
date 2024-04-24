package org.projectcheckins.security;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.generator.AccessTokenConfiguration;
import io.micronaut.security.token.generator.TokenGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public abstract class AbstractRegisterService implements RegisterService, UserFetcher, PasswordService {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRegisterService.class);

    private final PasswordEncoder passwordEncoder;
    private final List<RegistrationCheck> registrationChecks;
    private final TeamInvitationRepository teamInvitationRepository;
    private final TokenGenerator tokenGenerator;
    private final BlockingTokenValidator blockingTokenValidator;
    private final AccessTokenConfiguration accessTokenConfiguration;
    private final ApplicationEventPublisher<PasswordResetEvent> passwordResetEventPublisher;

    protected AbstractRegisterService(
            PasswordEncoder passwordEncoder,
            List<RegistrationCheck> registrationChecks,
            TeamInvitationRepository teamInvitationRepository,
            BlockingTokenValidator blockingTokenValidator,
            TokenGenerator tokenGenerator,
            AccessTokenConfiguration accessTokenConfiguration,
            ApplicationEventPublisher<PasswordResetEvent> passwordResetEventPublisher
    ) {
        this.passwordEncoder = passwordEncoder;
        this.registrationChecks = registrationChecks;
        this.teamInvitationRepository = teamInvitationRepository;
        this.blockingTokenValidator = blockingTokenValidator;
        this.tokenGenerator = tokenGenerator;
        this.accessTokenConfiguration = accessTokenConfiguration;
        this.passwordResetEventPublisher = passwordResetEventPublisher;
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

    @Override
    public Optional<@Email String> resetPassword(@NonNull @NotBlank String token, @NonNull @NotBlank String newRawPassword) {
        return blockingTokenValidator.validateToken(token).flatMap(auth -> findByEmail(auth.getName())
                .map(user -> {
                    updatePassword(user.getId(), newRawPassword);
                    return auth.getName();
                }));
    }

    @Override
    public void sendResetInstructions(@NonNull @NotBlank @Email String email, @NonNull Locale locale, @NonNull UriBuilder resetPasswordUri) {
        tokenGenerator.generateToken(Authentication.build(email), accessTokenConfiguration.getExpiration())
                .map(token -> resetPasswordUri.queryParam(TOKEN_QUERY_PARAM, token).toString())
                .map(url -> new PasswordResetEvent(email, locale, url))
                .ifPresent(event -> {
                    LOG.info("Publishing password reset event for: {} with URL: {}", event.getEmail(), event.getUrl());
                    passwordResetEventPublisher.publishEventAsync(event);
                });
    }

    @NonNull
    protected abstract void updatePassword(@NonNull @NotNull @Valid PasswordUpdate passwordUpdate);
}
