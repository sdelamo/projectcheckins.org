package org.projectcheckins.test;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import org.reactivestreams.Publisher;

import java.util.Collections;

public abstract class AbstractAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

    public static final Authentication ADMIN = Authentication.build("admin", Collections.singletonList("ROLE_ADMIN"), Collections.singletonMap("email", "admin@unityfoundation.io"));
    public static final Authentication SDELAMO = Authentication.build("xxx", Collections.emptyList(), Collections.singletonMap("email", "delamos@unityfoundation.io"));
    private Authentication authentication;

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        return Publishers.just(authentication);
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
