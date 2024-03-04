package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.config.RedirectConfiguration;
import io.micronaut.security.config.RedirectService;
import io.micronaut.security.errors.PriorToLoginPersistence;
import io.micronaut.security.session.SessionAuthenticationModeCondition;
import io.micronaut.security.session.SessionLoginHandler;
import io.micronaut.session.Session;
import io.micronaut.session.SessionStore;
import jakarta.inject.Singleton;

@Requires(
        condition = SessionAuthenticationModeCondition.class
)
@Requires(classes = SessionLoginHandler.class)
@Singleton
@Replaces(SessionLoginHandler.class)
public class SessionLoginHandlerReplacement extends SessionLoginHandler {
    public SessionLoginHandlerReplacement(RedirectConfiguration redirectConfiguration,
                                          SessionStore<Session> sessionStore,
                                          @Nullable PriorToLoginPersistence<HttpRequest<?>,
                                                  MutableHttpResponse<?>> priorToLoginPersistence,
                                          RedirectService redirectService) {
        super(redirectConfiguration, sessionStore,  priorToLoginPersistence, redirectService);
    }

    @Override
    public MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
        return LoginFailureUtils.loginFailure(loginFailure, authenticationFailed)
                .map(HttpResponse::seeOther)
                .orElseGet(HttpResponse::unauthorized);
    }
}
