// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.api.PublicProfile;
import org.projectcheckins.security.forms.TeamMemberDelete;
import org.projectcheckins.security.forms.TeamMemberSave;
import org.projectcheckins.security.forms.TeamInvitationDelete;
import org.projectcheckins.security.forms.TeamMemberUpdate;
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
import java.util.Locale;
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
    static final String URI_DELETE = UriBuilder.of("/team").path("delete").build().toString();
    static final String URI_UNINVITE = UriBuilder.of("/team").path("uninvite").build().toString();
    static final String URI_UPDATE = UriBuilder.of("/team").path("update").build().toString();

    static final PublicProfile USER_1 = new PublicProfileRecord(
            "user1",
            "user1@email.com",
            "User One",
            true
    );

    static final PublicProfile USER_2 = new PublicProfileRecord(
            "user2",
            "user2@email.com",
            "",
            false
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

    @Inject
    AuthenticationFetcherMock authMock;

    @Test
    void testListTeamMembers(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_LIST), String.class))
                .satisfies(htmlPage())
                .satisfies(htmlBody("""
                        <span>User One</span>"""))
                .satisfies(htmlBody("""
                        <form action="/team/delete" method="post">"""))
                .satisfies(htmlBody("""
                        <form action="/team/update" method="post">"""))
                .satisfies(htmlBody("Revoke Admin privileges"))
                .satisfies(htmlBody("Grant Admin privileges"))
                .satisfies(htmlBody("""
                        <code>user2@email.com</code>"""))
                .satisfies(htmlBody("""
                        <code>pending@email.com</code>"""))
                .satisfies(htmlBody(Pattern.compile("""
                        <a href="/team/create">""")));
    }

    @Test
    void testListTeamMembersNonAdmin(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_LIST), String.class))
                .satisfies(htmlPage())
                .satisfies(htmlBody("""
                        <span>User One</span>"""))
                .satisfies(htmlBody("""
                        <code>user2@email.com</code>"""))
                .satisfies(htmlBody(body -> Assertions.assertThat(body)
                        .doesNotContain("<code>pending@email.com</code>")
                        .doesNotContain("""
                                <form action="/team/delete" method="post">""")
                        .doesNotContain("""
                                <form action="/team/update" method="post">""")
                        .doesNotContain("""
                                <a href="/team/create">""")
                        .doesNotContain("Revoke Admin privileges")
                        .doesNotContain("Grant Admin privileges")
                ));
    }

    @Test
    void testCreateTeamMember(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_CREATE), String.class))
                .satisfies(htmlPage())
                .satisfies(htmlBody("""
                        <a href="/">"""))
                .satisfies(htmlBody("""
                        <a href="/team/list">"""))
                .satisfies(htmlBody("""
                        <form action="/team/save" method="post">"""))
                .satisfies(htmlBody("""
                        <input type="email" name="email" value="" id="email" class="form-control" required="required"/>"""));
    }

    @Test
    void testCreateTeamMemberNonAdmin(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_CREATE), String.class))
                .satisfies(redirection("/unauthorized"));
    }

    @Test
    void testSaveTeamMember(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        final Map<String, Object> body = Map.of("email", "user3@email.com");
        final HttpRequest<?> request = BrowserRequest.POST(URI_SAVE, body);
        Assertions.assertThat(client.exchange(request))
                .satisfies(status(HttpStatus.SEE_OTHER))
                .satisfies(location("/team/list"));
    }

    @Test
    void testSaveTeamMemberNonAdmin(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        final Map<String, Object> body = Map.of("email", "user3@email.com");
        final HttpRequest<?> request = BrowserRequest.POST(URI_SAVE, body);
        Assertions.assertThat(client.exchange(request))
                .satisfies(redirection("/unauthorized"));
    }

    @Test
    void testSaveTeamMemberInvalidEmail(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        final Map<String, Object> body = Map.of("email", "Invalid Email");
        final HttpRequest<?> request = BrowserRequest.POST(URI_SAVE, body);
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(request))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void testUninviteTeamMemberFormIllegalEmail(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        final String email = "*** illegal email ***";
        Assertions.assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of(URI_UNINVITE).queryParam("email", email).toString()), String.class))
                .satisfies(redirection(URI_LIST));
    }

    @Test
    void testUninviteTeamMember(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        final String email = INVITATION_1.email();
        Assertions.assertThat(client.exchange(BrowserRequest.POST(URI_UNINVITE, Map.of("email", email)), String.class))
                .satisfies(redirection(URI_LIST));
    }

    @Test
    void testUninviteTeamMemberNonAdmin(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        final String email = INVITATION_1.email();
        Assertions.assertThat(client.exchange(BrowserRequest.POST(URI_UNINVITE, Map.of("email", email)), String.class))
                .satisfies(redirection("/unauthorized"));
    }

    @Test
    void testRemoveTeamMemberIllegalEmail(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        final String email = "*** illegal email ***";
        Assertions.assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of(URI_DELETE).queryParam("email", email).toString()), String.class))
                .satisfies(redirection(URI_LIST));
    }

    @Test
    void testRemoveTeamMember(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        final Map<String, Object> body = Map.of("email", "user3@email.com");
        final HttpRequest<?> request = BrowserRequest.POST(URI_DELETE, body);
        Assertions.assertThat(client.exchange(request))
                .satisfies(redirection(URI_LIST));
    }

    @Test
    void testRemoveTeamMemberNonAdmin(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        final Map<String, Object> body = Map.of("email", "user3@email.com");
        final HttpRequest<?> request = BrowserRequest.POST(URI_DELETE, body);
        Assertions.assertThat(client.exchange(request))
                .satisfies(redirection("/unauthorized"));
    }

    @Test
    void testMemberUpdate(@Client("/") HttpClient httpClient) {
        final BlockingHttpClient client = httpClient.toBlocking();
        authMock.setAuthentication(AbstractAuthenticationFetcher.ADMIN);
        final Map<String, Object> body = Map.of("email", "user3@email.com", "isAdmin", true);
        final HttpRequest<?> request = BrowserRequest.POST(URI_UPDATE, body);
        Assertions.assertThat(client.exchange(request, String.class))
                .satisfies(redirection(URI_LIST));
    }

    @Requires(property = "spec.name", value = "TeamControllerTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
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
        public void save(@NotNull @Valid TeamMemberSave form, @Nullable Tenant tenant, @NotNull Locale locale, @NotBlank String signupUrl) {
        }

        @Override
        public void remove(@NotNull @Valid TeamMemberDelete form, @Nullable Tenant tenant) {

        }

        @Override
        public void uninvite(@NotNull @Valid TeamInvitationDelete form, @Nullable Tenant tenant) {

        }

        @Override
        public void update(@NotNull @Valid TeamMemberUpdate form, @Nullable Tenant tenant) {}
    }

    record PublicProfileRecord(String id, String email, String fullName, boolean isAdmin) implements PublicProfile {
    }
}
