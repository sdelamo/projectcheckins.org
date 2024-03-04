package org.projectcheckins.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestExecutorAuthenticationProvider;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.List;

import static io.micronaut.security.authentication.AuthenticationFailureReason.*;

@Requires(beans = { UserFetcher.class, PasswordEncoder.class, AuthoritiesFetcher.class })
@Singleton
class DelegatingAuthenticationProvider<B> implements HttpRequestExecutorAuthenticationProvider<B> {

    public static final String ATTRIBUTES_EMAIL = "email";
    private final UserFetcher userFetcher;
    private final List<PasswordEncoder> passwordEncoders;
    private final AuthoritiesFetcher authoritiesFetcher;

    DelegatingAuthenticationProvider(UserFetcher userFetcher,
                                     List<PasswordEncoder> passwordEncoders,
                                     AuthoritiesFetcher authoritiesFetcher) {
        this.userFetcher = userFetcher;
        this.passwordEncoders = passwordEncoders;
        this.authoritiesFetcher = authoritiesFetcher;
    }

    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext,
                                                        @NonNull AuthenticationRequest<String, String> authRequest) {
            UserState user = fetchUserState(authRequest);
            AuthenticationFailureReason reason = validate(user, authRequest);

            return reason != null
                    ? AuthenticationResponse.failure(reason)
                    : createSuccessfulAuthenticationResponse(user);
    }

    @Nullable
    private AuthenticationFailureReason validate(@Nullable UserState user,
                                          @NonNull AuthenticationRequest<?, ?> authenticationRequest) {
        if (user == null) {
            return USER_NOT_FOUND;
        }
        if (!user.isEnabled()) {
            return USER_DISABLED;
        }
        for (PasswordEncoder passwordEncoder : passwordEncoders) {
            if (passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
                return null;
            }
        }
        return CREDENTIALS_DO_NOT_MATCH;
    }

    @Nullable
    private UserState fetchUserState(AuthenticationRequest<?, ?> authRequest) {
        final Object email = authRequest.getIdentity();
        return userFetcher.findByEmail(email.toString()).orElse(null);
    }

    @NonNull
    private AuthenticationResponse createSuccessfulAuthenticationResponse(@NonNull UserState user) {
        List<String> authorities = authoritiesFetcher.findAuthoritiesByEmail(user.getEmail());
        return AuthenticationResponse.success(user.getId(),
                authorities,
                Collections.singletonMap(ATTRIBUTES_EMAIL, user.getEmail()));
    }

}
