package org.projectcheckins.security;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.function.Predicate.not;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Property(name = "spec.name", value = "DelegatingAuthenticationProviderTest")
@MicronautTest(startApplication = false)
class DelegatingAuthenticationProviderTest {

    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";
    private final static String NOT_ENABLED = "disabled@unityfoundation.io";
    private static final String CORRECT_PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrongpassword";

    @Test
    void testUserNotFoundAuthentication(DelegatingAuthenticationProvider<?> authenticationProvider) {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(NOT_FOUND_EMAIL, CORRECT_PASSWORD);
        assertThat(authenticationProvider.authenticate(null, credentials))
            .matches(not(AuthenticationResponse::isAuthenticated))
            .isInstanceOf(AuthenticationFailed.class)
            .extracting(c -> ((AuthenticationFailed) c).getReason())
            .isEqualTo(AuthenticationFailureReason.USER_NOT_FOUND);
    }

    @Test
    void testUserCredentialsDontMatch(DelegatingAuthenticationProvider<?> authenticationProvider) {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(FOUND_EMAIL, WRONG_PASSWORD);
        assertThat(authenticationProvider.authenticate(null, credentials))
            .matches(not(AuthenticationResponse::isAuthenticated))
            .isInstanceOf(AuthenticationFailed.class)
            .extracting(c -> ((AuthenticationFailed) c).getReason())
            .isEqualTo(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
    }

    @Test
    void testUserFoundAuthentication(DelegatingAuthenticationProvider<?> authenticationProvider) {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(FOUND_EMAIL, CORRECT_PASSWORD);
        assertThat(authenticationProvider.authenticate(null, credentials))
            .matches(AuthenticationResponse::isAuthenticated)
            .isNotInstanceOf(AuthenticationFailed.class);
    }

    @Test
    void testUserDisabledAuthentication(DelegatingAuthenticationProvider<?> authenticationProvider) {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(NOT_ENABLED, CORRECT_PASSWORD);
        assertThat(authenticationProvider.authenticate(null, credentials))
                .matches(not(AuthenticationResponse::isAuthenticated))
                .isInstanceOf(AuthenticationFailed.class)
                .extracting(c -> ((AuthenticationFailed) c).getReason())
                .isEqualTo(AuthenticationFailureReason.USER_DISABLED);
    }


    @Requires(property = "spec.name", value = "DelegatingAuthenticationProviderTest")
    @Singleton
    static class AuthenticationFetcherMock implements AuthoritiesFetcher {
        @Override
        public List<String> findAuthoritiesByEmail(@NonNull @NotBlank String email) {
            return Collections.emptyList();
        }
    }

    @Requires(property = "spec.name", value = "DelegatingAuthenticationProviderTest")
    @Singleton
    static class UserFetcherMock implements UserFetcher {

        private final String encodedPassword;
        UserFetcherMock(PasswordEncoder passwordEncoder) {
            encodedPassword = passwordEncoder.encode(CORRECT_PASSWORD);
        }

        @Override
        @NonNull
        public Optional<UserState> findByEmail(@NotBlank @NonNull String email) {
            if (email.equals(FOUND_EMAIL) || email.equals(NOT_ENABLED)) {
                return Optional.of(new UserState() {
                    @Override
                    public String getId() {
                        return "xxx";
                    }

                    @Override
                    public boolean isEnabled() {
                        return !email.equals(NOT_ENABLED);
                    }

                    @Override
                    public String getEmail() {
                        return FOUND_EMAIL;
                    }

                    @Override
                    public String getPassword() {
                        return encodedPassword;
                    }
                });
            }
            return Optional.empty();
        }
    }


}