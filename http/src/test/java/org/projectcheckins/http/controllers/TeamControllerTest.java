package org.projectcheckins.http.controllers;

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
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.services.TeamService;
import org.projectcheckins.core.services.TeamServiceImpl;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserFetcher;
import org.projectcheckins.security.UserState;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;
import org.projectcheckins.test.HttpClientResponseExceptionAssert;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.projectcheckins.test.AssertUtils.*;

@MicronautTest
@Property(name = "spec.name", value = "TeamControllerTest")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
class TeamControllerTest {

    static final String URI_LIST = UriBuilder.of("/team").path("list").build().toString();
    static final String URI_CREATE = UriBuilder.of("/team").path("create").build().toString();
    static final String URI_SAVE = UriBuilder.of("/team").path("save").build().toString();

    static final Profile USER_1 = new ProfileRecord(
            "user1",
            "user1@email.com",
            TimeZone.getDefault(),
            DayOfWeek.MONDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
            Format.MARKDOWN,
            "User",
            "One"
    );

    static final Profile USER_2 = new ProfileRecord(
            "user2",
            "user2@email.com",
            TimeZone.getDefault(),
            DayOfWeek.SUNDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWELVE_HOUR_CLOCK,
            Format.WYSIWYG,
            null,
            null
    );

    @Test
    void testListTeamMembers(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_LIST), String.class))
                .matches(htmlPage())
                .matches(htmlBody("""
                        <span>User One</span>"""))
                .matches(htmlBody("""
                        <code>user2@email.com</code>"""))
                .matches(htmlBody(Pattern.compile("""
                        <a href="/team/create">""")));
    }

    @Test
    void testCreateTeamMember(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        Assertions.assertThat(client.exchange(BrowserRequest.GET(URI_CREATE), String.class))
                .matches(htmlPage())
                .matches(htmlBody("""
                        <a class="nav-link" aria-current="page" href="/team/list">"""))
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

    @Test
    void testSaveTeamMemberAlreadyExists(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final Map<String, Object> body = Map.of("email", USER_1.email());
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
        @NonNull
        public Optional<UserState> findByEmail(@NotBlank @NonNull String email) {
            return switch (email) {
                case "user1@email.com" -> Optional.of(new UserStateRecord(USER_1));
                case "user2@email.com" -> Optional.of(new UserStateRecord(USER_2));
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
        public String save(@NotNull @Valid TeamMemberSave form, @Nullable Tenant tenant) throws UserAlreadyExistsException {
            return "xxx";
        }
    }

    record UserStateRecord(Profile profile) implements UserState {
        @Override
        public String getId() {
            return profile.id();
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getEmail() {
            return profile.email();
        }

        @Override
        public String getPassword() {
            return "secret";
        }
    }
}
