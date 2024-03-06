package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
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
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.test.BrowserRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.*;

@Property(name = "spec.name", value = "SecurityControllerTest")


@MicronautTest
@Property(name = "micronaut.security.authentication", value="cookie")
@Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret", value="pleaseChangeThisSecretForANewOne")
@Property(name = "micronaut.security.redirect.login-failure", value="/security/login")
@Property(name = "spec.name", value="SecurityControllerTest")
class SecurityControllerTest {
    private static final String EMAIL_ALREADY_EXISTS = "delamos@unityfoundation.io";
    private static final String NEW_USER_EMAIL = "calvog@unityfoundation.io";
    private static final HttpRequest<?> NOT_MATCHING_PASSWORD_REQUEST = BrowserRequest.POST("/security/signUp", Map.of("email", EMAIL_ALREADY_EXISTS, "password", "password", "repeatPassword", "bogus"));
    private static final HttpRequest<?> EMAIL_ALREADY_EXISTS_REQUEST = BrowserRequest.POST("/security/signUp", Map.of("email", EMAIL_ALREADY_EXISTS, "password", "password", "repeatPassword", "password"));
    private static final HttpRequest<?> NEW_USER_REQUEST = BrowserRequest.POST("/security/signUp", Map.of("email", NEW_USER_EMAIL, "password", "password", "repeatPassword", "password"));
    public static final String TYPE_PASSWORD = "type=\"password\"";
    public static final String TYPE_EMAIL = """
            type="email""";
    public static final String ACTION_SECURITY_SIGN_UP = "action=\"/security/signUp\"";
    public static final String ACTION_SECURITY_LOGIN = "action=\"/login\"";

    @Test
    void login(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.retrieve(BrowserRequest.GET("/security/login")))
                .containsOnlyOnce(ACTION_SECURITY_LOGIN)
                .containsOnlyOnce(TYPE_EMAIL)
                .containsOnlyOnce(TYPE_PASSWORD);

        assertThat(client.retrieve(HttpRequest.POST("/login", Map.of("username", "sherlock@example.com", "password", "password"))))
                .containsOnlyOnce(ACTION_SECURITY_LOGIN)
                .containsOnlyOnce(TYPE_EMAIL)
                .containsOnlyOnce(TYPE_PASSWORD)
                        .contains("User disabled. Verify your email address first.");

        assertThat(client.retrieve(HttpRequest.POST("/login", Map.of("username", "watson@example.com", "password", "password"))))
                .containsOnlyOnce(ACTION_SECURITY_LOGIN)
                .containsOnlyOnce(TYPE_EMAIL)
                .containsOnlyOnce(TYPE_PASSWORD)
                .contains("The username or password is incorrect. Please try again.");
    }

    @Test
    void signUp(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();

        assertThat(client.retrieve(BrowserRequest.GET("/security/signUp")))
                .satisfies(containsManyTimes(2, TYPE_PASSWORD))
                .containsOnlyOnce(TYPE_EMAIL)
                .containsOnlyOnce(ACTION_SECURITY_SIGN_UP);

        assertThat(client.retrieve(NOT_MATCHING_PASSWORD_REQUEST))
                .satisfies(containsManyTimes(2, TYPE_PASSWORD))
                .containsOnlyOnce(TYPE_EMAIL)
                .containsOnlyOnce(ACTION_SECURITY_SIGN_UP)
                .containsOnlyOnce("Passwords do not match");

        Argument<String> ok = Argument.of(String.class);
        Argument<String> ko = Argument.of(String.class);
        assertThrowsWithHtml(() -> client.retrieve(EMAIL_ALREADY_EXISTS_REQUEST, ok, ko), HttpStatus.UNPROCESSABLE_ENTITY)
                .satisfies(containsManyTimes(2, TYPE_PASSWORD))
                .satisfies(containsOnlyOnce(TYPE_EMAIL))
                .satisfies(containsOnlyOnce(ACTION_SECURITY_SIGN_UP))
                .satisfies(containsOnlyOnce("User already exists"));

        assertThat(client.retrieve(NEW_USER_REQUEST))
                .containsOnlyOnce(ACTION_SECURITY_LOGIN)
                .containsOnlyOnce(TYPE_EMAIL)
                .containsOnlyOnce(TYPE_PASSWORD);
    }

    @Requires(property = "spec.name", value = "SecurityControllerTest")
    @Singleton
    static class RegisterServiceMock implements RegisterService {
        private final List<String> emails = new ArrayList<>();

        @Override
        public String register(String email, String rawPassword, List<String> authorities) throws UserAlreadyExistsException {
            if (email.equals(EMAIL_ALREADY_EXISTS)) {
                throw new UserAlreadyExistsException();
            }
            emails.add(email);
            return "xxx";
        }

        public List<String> getEmails() {
            return emails;
        }
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
