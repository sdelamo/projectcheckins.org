package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.authentication.provider.HttpRequestExecutorAuthenticationProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Property(name = "micronaut.security.authentication", value="session")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "micronaut.security.redirect.login-failure", value="/security/login")
@Property(name = "spec.name", value="SessionLoginHandlerReplacementTest")
@MicronautTest
class SessionLoginHandlerReplacementTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> response = client.exchange(HttpRequest.POST("/login", new UsernamePasswordCredentials("watson@example.com", "password")));
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION))
            .isEqualTo("/security/login?reason=USER_DISABLED");
    }

    @Requires(property = "spec.name", value="SessionLoginHandlerReplacementTest")
    @Singleton
    static class AuthenticationProviderMock<B> implements HttpRequestExecutorAuthenticationProvider<B> {
        @Override
        public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {
            return AuthenticationResponse.failure(AuthenticationFailureReason.USER_DISABLED);
        }
    }

}