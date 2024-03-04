package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationResponse;

import java.net.URI;
import java.util.Optional;

public final class LoginFailureUtils {
    private LoginFailureUtils() {
    }

    @NonNull
    public static Optional<URI> loginFailure(@Nullable String loginFailure,
                               @NonNull AuthenticationResponse authenticationFailed) {
        if (loginFailure == null) {
            return Optional.empty();
        }
        UriBuilder uriBuilder = UriBuilder.of(loginFailure);
        if (authenticationFailed instanceof AuthenticationFailed failure) {
            uriBuilder = uriBuilder.queryParam("reason", failure.getReason());
        }
        return Optional.of(uriBuilder.build());

    }
}
