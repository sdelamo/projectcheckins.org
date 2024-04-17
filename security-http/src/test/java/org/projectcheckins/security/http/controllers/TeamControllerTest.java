package org.projectcheckins.security.http.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.api.PublicProfile;
import org.projectcheckins.security.forms.TeamMemberSave;
import org.projectcheckins.security.services.TeamService;
import org.projectcheckins.security.services.TeamServiceImpl;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.UserFetcher;
import org.projectcheckins.security.UserState;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;
import org.projectcheckins.test.HttpClientResponseExceptionAssert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.projectcheckins.test.AssertUtils.*;

@MicronautTest
@Property(name = "spec.name", value = "TeamControllerTest")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
class TeamControllerTest {

    static final String URI_LIST = UriBuilder.of("/team").path("list").build().toString();
    static final String URI_CREATE = UriBuilder.of("/team").path("create").build().toString();
    static final String URI_SAVE = UriBuilder.of("/team").path("save").build().toString();

    static final PublicProfile USER_1 = new PublicProfileRecord(
            "user1",
            "user1@email.com",
            "User One"
    );

    static final PublicProfile USER_2 = new PublicProfileRecord(
            "user2",
            "user2@email.com",
            ""
    );

    static final UserState USER_STATE_1 = new UserState() {
        @Override
        public String getId() {
            return USER_1.id();
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getEmail() {
            return USER_1.email();
        }

        @Override
        public String getPassword() {
            return "password";
        }
    };

    static final UserState USER_STATE_2 = new UserState() {
        @Override
        public String getId() {
            return USER_2.id();
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getEmail() {
            return USER_2.email();
        }

        @Override
        public String getPassword() {
            return "password";
        }
    };

    static final TeamInvitation INVITATION_1 = new TeamInvitationRecord("pending@email.com", null);

    @Test
    void testListTeamMembers(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_LIST), String.class))
                .matches(htmlPage())
                .matches(htmlBody("""
                        <span>User One</span>"""))
                .matches(htmlBody("""
                        <code>user2@email.com</code>"""))
                .matches(htmlBody("""
                        <code>pending@email.com</code>"""))
                .matches(htmlBody(Pattern.compile("""
                        <a href="/team/create">""")));
    }

    @Test
    void testCreateTeamMember(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_CREATE), String.class))
                .matches(htmlPage())
                .matches(htmlBody("""
                        <a href="/">"""))
                .matches(htmlBody("""
                        <a href="/team/list">"""))
                .matches(htmlBody("""
                        <form action="/team/save" method="post">"""))
                .matches(htmlBody("""
                        <input type="email" name="email" value="" id="email" class="form-control" required="required"/>"""));
    }

    @Test
    void testSaveTeamMember(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final Map<String, Object> body = Map.of("email", "user3@email.com");
        final HttpRequest<?> request = BrowserRequest.POST(URI_SAVE, body);
        Assertions.assertThat(client.exchange(request))
                .matches(status(HttpStatus.SEE_OTHER))
                .matches(location("/team/list"));
    }

    @Test
    void testSaveTeamMemberInvalidEmail(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final Map<String, Object> body = Map.of("email", "Invalid Email");
        final HttpRequest<?> request = BrowserRequest.POST(URI_SAVE, body);
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(request))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Requires(property = "spec.name", value = "TeamControllerTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
        AuthenticationFetcherMock() {
            setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        }
    }

    @Requires(property = "spec.name", value = "TeamControllerTest")
    @Singleton
    static class UserFetcherMock implements UserFetcher {

        @Override
        @NonNull
        public Optional<UserState> findById(@NotBlank @NonNull String id) {
            return Optional.empty();
        }

        @NonNull
        public Optional<UserState> findByEmail(@NotBlank @NonNull String email) {
            return switch (email) {
                case "user1@email.com" -> Optional.of(USER_STATE_1);
                case "user2@email.com" -> Optional.of(USER_STATE_2);
                default -> Optional.empty();
            };
        }
    }

    @Requires(property = "spec.name", value = "TeamControllerTest")
    @Singleton
    @Replaces(TeamServiceImpl.class)
    static class TeamServiceMock implements TeamService {

        @Override
        public List<? extends PublicProfile> findAll(@Nullable Tenant tenant) {
            return List.of(USER_1, USER_2);
        }

        @Override
        public List<? extends TeamInvitation> findInvitations(Tenant tenant) {
            return List.of(INVITATION_1);
        }

        @Override
        public void save(@NotNull @Valid TeamMemberSave form, @Nullable Tenant tenant) {
        }
    }

    record PublicProfileRecord(String id, String email, String fullName) implements PublicProfile {
    }
}
