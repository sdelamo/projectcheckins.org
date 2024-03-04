package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestExecutorAuthenticationProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
@Property(name = "micronaut.security.authentication", value="cookie")
@Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret", value="pleaseChangeThisSecretForANewOne")
@Property(name = "micronaut.security.redirect.login-failure", value="/security/login")
@Property(name = "spec.name", value="SecurityControllerTest")
class SecurityControllerTest {

    @Test
    void login(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.retrieve(BrowserRequest.GET("/security/login")))
            .matches(html -> html.contains("""
    type="password"""));

        String html = client.retrieve(HttpRequest.POST("/login", Map.of("username", "sherlock@example.com", "password", "password")));
        assertThat(html).contains("""
    User disabled. Verify your email address first.""");
        html = client.retrieve(HttpRequest.POST("/login", Map.of("username", "watson@example.com", "password", "password")));
        assertThat(html).contains("""    
    The username or password is incorrect. Please try again.""");
    }


    @Requires(property = "spec.name", value="SecurityControllerTest")
    @Singleton
    static class AuthenticationProviderMock<B> implements HttpRequestExecutorAuthenticationProvider<B> {
        @Override
        public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {
            if (authRequest.getIdentity().equals("sherlock@example.com")) {
                return AuthenticationResponse.failure(AuthenticationFailureReason.USER_DISABLED);
            } else {
                return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
            }
        }
    }
}
