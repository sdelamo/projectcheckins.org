package org.projectcheckins.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestExecutorAuthenticationProvider;
import io.micronaut.security.authentication.provider.HttpRequestReactiveAuthenticationProvider;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import static io.micronaut.security.authentication.AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH;
import static io.micronaut.security.authentication.AuthenticationFailureReason.USER_NOT_FOUND;

@Requires(beans = { UserFetcher.class, PasswordEncoder.class, AuthoritiesFetcher.class })
@Singleton
class DelegatingAuthenticationProvider<B> implements HttpRequestExecutorAuthenticationProvider<B> {

    public static final String ATTRIBUTES_EMAIL = "email";
    private final UserFetcher userFetcher;
    private final List<PasswordEncoder> passwordEncoders;
    private final AuthoritiesFetcher authoritiesFetcher;
    private final Scheduler scheduler;

    DelegatingAuthenticationProvider(UserFetcher userFetcher,
                                     List<PasswordEncoder> passwordEncoders,
                                     AuthoritiesFetcher authoritiesFetcher,
                                     @Named(TaskExecutors.BLOCKING) ExecutorService executorService) {
        this.userFetcher = userFetcher;
        this.passwordEncoders = passwordEncoders;
        this.authoritiesFetcher = authoritiesFetcher;
        this.scheduler = Schedulers.fromExecutorService(executorService);
    }

    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext,
                                                        @NonNull AuthenticationRequest<String, String> authRequest) {
            UserState user = fetchUserState(authRequest);
            AuthenticationFailed authenticationFailed = validate(user, authRequest);

            return authenticationFailed != null
                    ? AuthenticationResponse.failure(authenticationFailed.getReason())
                    : createSuccessfulAuthenticationResponse(user);
    }

    @Nullable
    private AuthenticationFailed validate(@Nullable UserState user,
                                          @NonNull AuthenticationRequest<?, ?> authenticationRequest) {
        if (user == null) {
            return new AuthenticationFailed(USER_NOT_FOUND);
        }
        for (PasswordEncoder passwordEncoder : passwordEncoders) {
            if (passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
                return null;
            }
        }
        return new AuthenticationFailed(CREDENTIALS_DO_NOT_MATCH);
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
