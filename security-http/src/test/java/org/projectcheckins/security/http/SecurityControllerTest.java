package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestExecutorAuthenticationProvider;
import io.micronaut.security.token.generator.TokenGenerator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.AbstractRegisterService;
import org.projectcheckins.security.BlockingTokenValidator;
import org.projectcheckins.security.BlockingTokenValidatorImpl;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.PasswordEncoder;
import org.projectcheckins.security.PasswordUpdate;
import org.projectcheckins.security.UserFetcher;
import org.projectcheckins.security.UserState;
import org.projectcheckins.security.UserSave;
import org.projectcheckins.security.RegistrationCheckViolationException;
import org.projectcheckins.security.UserAlreadyExistsRegistrationCheck;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AbstractAuthenticationFetcher.SDELAMO;
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
    private static final UserState USER_STATE = new UserState() {
        @Override
        public String getId() {
            return SDELAMO.getName();
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getEmail() {
            return EMAIL_ALREADY_EXISTS;
        }

        @Override
        public String getPassword() {
            return "secret";
        }
    };
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

    @Test
    void changePassword(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.retrieve(BrowserRequest.GET("/security/changePassword")))
                .satisfies(containsManyTimes(3, TYPE_PASSWORD))
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/">""")
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/profile/show">""")
                .containsOnlyOnce("""
                        action="/security/updatePassword" method="post">""");
    }

    @Test
    void updatePassword(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/updatePassword", Map.of(
                "userId", SDELAMO.getName(),
                "currentPassword", "old password",
                "password", "new password",
                "repeatPassword", "new password"));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/">""")
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/profile/show">""")
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/security/changePassword">""")
                .containsOnlyOnce("You have successfully changed your password.");
    }

    @Test
    void updatePasswordWrongPassword(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/updatePassword", Map.of(
                "userId", SDELAMO.getName(),
                "currentPassword", "wrong password",
                "password", "new password",
                "repeatPassword", "new password"));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/">""")
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/profile/show">""")
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/security/changePassword">""")
                .containsOnlyOnce("Current password is incorrect. Please try again.");
    }

    @Test
    void updatePasswordNewPasswordsDoNotMatch(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/updatePassword", Map.of(
                "userId", SDELAMO.getName(),
                "currentPassword", "old password",
                "password", "new password one",
                "repeatPassword", "new password two"));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/">""")
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/profile/show">""")
                .containsOnlyOnce("""
                        <li class="breadcrumb-item"><a href="/security/changePassword">""")
                .containsOnlyOnce("Passwords do not match");
    }

    @Test
    void updatePasswordWrongUserId(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/updatePassword", Map.of(
                "userId", "wrong user",
                "currentPassword", "old password",
                "password", "new password",
                "repeatPassword", "new password"));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("""
                        <form action="/login" method="post">""");
    }

    @Test
    void forgotPassword(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.retrieve(BrowserRequest.GET("/security/forgotPassword")))
                .containsOnlyOnce("""
                        <input type="email" name="email""")
                .containsOnlyOnce("""
                        action="/security/forgotPassword" method="post""");
    }

    @Test
    void sendResetInstructions(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/forgotPassword", Map.of("email", EMAIL_ALREADY_EXISTS));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("""
                        <form action="/login" method="post""")
                .containsOnlyOnce("Check your email for reset instructions");
    }

    @Test
    void sendResetInstructionsInvalidEmail(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/forgotPassword", Map.of("email", "invalid email"));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("must be a well-formed email address");
    }

    @Test
    void resetPasswordForm(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.retrieve(BrowserRequest.GET("/security/resetPassword?token=valid")))
                .satisfies(containsManyTimes(2, TYPE_PASSWORD))
                .containsOnlyOnce("""
                        <form action="/security/resetPassword" method="post""");
    }

    @Test
    void resetPasswordFormInvalidToken(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.retrieve(BrowserRequest.GET("/security/resetPassword?token=invalid")))
                .containsOnlyOnce("Token is invalid or already expired")
                .containsOnlyOnce("""
                        <form action="/login" method="post""");
    }

    @Test
    void resetPassword(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/resetPassword", Map.of(
                "token", "valid",
                "password", "new password",
                "repeatPassword", "new password"));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("You have successfully reset your password")
                .containsOnlyOnce("""
                        <form action="/login" method="post""");
    }

    @Test
    void resetPasswordsNewPasswordsDoNotMatch(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final HttpRequest<?> request = BrowserRequest.POST("/security/resetPassword", Map.of(
                "token", "valid",
                "password", "new password one",
                "repeatPassword", "new password two"));
        assertThat(client.retrieve(request))
                .containsOnlyOnce("Passwords do not match")
                .containsOnlyOnce("""
                        <form action="/security/resetPassword" method="post""");
    }

    @Requires(property = "spec.name", value = "SecurityControllerTest")
    @Singleton
    static class RegisterServiceMock implements RegisterService {
        private final List<String> emails = new ArrayList<>();

        @Override
        public String register(String email, String rawPassword, @Nullable Tenant tenant, List<String> authorities) throws RegistrationCheckViolationException {
            if (email.equals(EMAIL_ALREADY_EXISTS)) {
                throw new RegistrationCheckViolationException(UserAlreadyExistsRegistrationCheck.VIOLATION_USER_ALREADY_EXISTS);
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

    @Requires(property = "spec.name", value = "SecurityControllerTest")
    @Secondary
    @Singleton
    static class PasswordServiceMock extends AbstractRegisterService {
        protected PasswordServiceMock(TokenGenerator tokenGenerator) {
            super(new PasswordEncoderMock(), null, null, new BlockingTokenValidatorMock(), tokenGenerator, () -> 600, x -> {});
        }

        @Override
        protected String register(UserSave userSave, Tenant tenant) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        protected void updatePassword(PasswordUpdate passwordUpdate) {
            // do nothing
        }

        @Override
        public Optional<UserState> findById(String id) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public Optional<UserState> findByEmail(String email) {
            return Optional.of(USER_STATE);
        }
    }

    @Requires(property = "spec.name", value = "SecurityControllerTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
        AuthenticationFetcherMock() {
            setAuthentication(SDELAMO);
        }
    }

    @Requires(property = "spec.name", value = "SecurityControllerTest")
    @Singleton
    static class UserFetcherMock implements UserFetcher {

        @Override
        public Optional<UserState> findById(String id) {
            return Optional.of(new UserState() {
                    @Override
                    public String getId() {
                        return id;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }

                    @Override
                    public String getEmail() {
                        return "sdelamo@email.com";
                    }

                    @Override
                    public String getPassword() {
                        return "old password";
                    }
                });
        }

        @Override
        public Optional<UserState> findByEmail(String email) {
            return Optional.empty();
        }
    }

    @Requires(property = "spec.name", value = "SecurityControllerTest")
    @Primary
    @Singleton
    static class PasswordEncoderMock implements PasswordEncoder {

        @Override
        public String encode(String rawPassword) {
            return rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String encodedPassword) {
            return rawPassword.equals(encodedPassword);
        }
    }

    @Requires(property = "spec.name", value = "SecurityControllerTest")
    @Singleton
    @Replaces(BlockingTokenValidatorImpl.class)
    static class BlockingTokenValidatorMock implements BlockingTokenValidator {
        @Override
        public Optional<Authentication> validateToken(String token) {
            return "valid".equals(token) ? Optional.of(new ClientAuthentication(EMAIL_ALREADY_EXISTS, null)) : Optional.empty();
        }
    }
}
