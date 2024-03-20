package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.test.AbstractAuthenticationFetcher.SDELAMO;
import static org.projectcheckins.test.AssertUtils.redirection;
import static org.projectcheckins.test.HttpClientResponseExceptionAssert.*;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.QuestionSave;
import org.projectcheckins.core.api.QuestionUpdate;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;
import org.projectcheckins.test.HttpClientResponseExceptionAssert;
import org.reactivestreams.Publisher;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "QuestionControllerFormTest")
@MicronautTest
class QuestionControllerFormTest {
    @Requires(property = "spec.name", value = "QuestionControllerFormTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
    }

    @Requires(property = "spec.name", value = "QuestionControllerFormTest")
    @Singleton
    static class ProfileRepositoryMock extends SecondaryProfileRepository {
        @Override
        public Optional<? extends Profile> findByAuthentication(Authentication authentication, Tenant tenant) {
            return Optional.of(new ProfileRecord(SDELAMO.getAttributes().get("email").toString(),
                    TimeZone.getDefault(),
                    DayOfWeek.MONDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(16, 30),
                    TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
                    Format.WYSIWYG,
                    null,
                    null));
        }
    }

    @Test
    void crud(@Client("/") HttpClient httpClient,
              QuestionRepository questionRepository,
              AuthenticationFetcherMock authenticationFetcher) {
        authenticationFetcher.setAuthentication(SDELAMO);
        BlockingHttpClient client = httpClient.toBlocking();
        String title = "What are you working on?";
        HttpResponse<?> saveResponse = client.exchange(BrowserRequest.POST("/question/save", "title="+title+"&howOften=DAILY_ON&dailyOnDay=MONDAY&dailyOnDay=TUESDAY&dailyOnDay=WEDNESDAY&dailyOnDay=THURSDAY&dailyOnDay=FRIDAY&timeOfDay=END&fixedTime=16:30"));
        assertThat(saveResponse)
            .matches(redirection(s -> s.startsWith("/question") && s.endsWith("/show")));

        String location = saveResponse.getHeaders().get(HttpHeaders.LOCATION);
        String id = location.substring(location.indexOf("/question/") + "/question/".length(), location.lastIndexOf("/show"));

        assertThat(client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path("list").build()), String.class))
            .contains(title)
            .contains("/question/create");

        assertThat(client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path(id).path("show").build()), String.class))
            .contains(title)
            .contains("/question/list")
            .contains("/question/" + id  + "/edit");

        assertThat(client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path(id).path("edit").build()), String.class))
            .contains(title);

        String updatedTitle = "What did you do today?";
        URI updateUri = UriBuilder.of("/question").path(id).path("update").build();
        String updateBody = "id=" + id + "&title="+updatedTitle+"&howOften=DAILY_ON&dailyOnDay=MONDAY&dailyOnDay=TUESDAY&dailyOnDay=WEDNESDAY&dailyOnDay=THURSDAY&dailyOnDay=FRIDAY&timeOfDay=END&fixedTime=16:30";

        assertThat(client.exchange(BrowserRequest.POST(updateUri.toString(), updateBody)))
            .matches(redirection(s -> s.equals("/question/" + id + "/show")));

        assertThat(client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path(id).path("edit").build()), String.class))
            .doesNotContain(title)
            .contains(updatedTitle);

        String updateBodyWithNotMatchingId = "id=yyy&title="+title+"&howOften=DAILY_ON&dailyOnDay=MONDAY&dailyOnDay=TUESDAY&dailyOnDay=WEDNESDAY&dailyOnDay=THURSDAY&dailyOnDay=FRIDAY&timeOfDay=END&fixedTime=16:30";

        assertThatThrowsHttpClientResponseException(() -> client.exchange(BrowserRequest.POST(updateUri.toString(), updateBodyWithNotMatchingId)))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        URI deleteUri = UriBuilder.of("/question").path(id).path("delete").build();
        assertThat(client.exchange(BrowserRequest.POST(deleteUri.toString(), Collections.emptyMap())))
            .matches(redirection("/question/list"));

        assertThat(questionRepository.findAll()).isEmpty();
    }

    @Requires(property = "spec.name", value = "QuestionControllerFormTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {

        Map<String, QuestionRecord> questions = new HashMap<>();

        @Override
        @NonNull
        public String save(@NotNull @Valid QuestionSave form, @Nullable Tenant tenant) {
            String id = "xxx";
            questions.put(id, new QuestionRecord(id, form.title(), form.howOften(), form.days(), form.timeOfDay(), form.fixedTime()));
            return id;
        }

        @Override
        @NonNull
        public Optional<QuestionRecord> findById(@NotBlank String id, @Nullable Tenant tenant) {
            return Optional.ofNullable(questions.get(id));
        }

        @Override
        public void update(@NotNull @Valid QuestionUpdate form, @Nullable Tenant tenant) {
            if (questions.containsKey(form.id())) {
                questions.put(form.id(), new QuestionRecord(form.id(),form.title(), form.howOften(), form.days(), form.timeOfDay(), form.fixedTime()));
            }
        }

        @Override
        @NonNull
        public List<QuestionRecord> findAll(@Nullable Tenant tenant) {
            return questions.values().stream().toList();
        }

        @Override
        public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
            questions.remove(id);
        }
    }
}
