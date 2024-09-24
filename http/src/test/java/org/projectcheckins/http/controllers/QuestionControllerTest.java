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

package org.projectcheckins.http.controllers;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AbstractAuthenticationFetcher.SDELAMO;
import static org.projectcheckins.test.AssertUtils.htmlPage;
import static org.projectcheckins.test.AssertUtils.htmlBody;
import static org.projectcheckins.test.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
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
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;
import org.projectcheckins.test.HttpClientResponseExceptionAssert;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "QuestionControllerTest")
@MicronautTest
class QuestionControllerTest {
    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    static class ProfileRepositoryMock extends SecondaryProfileRepository {
        @Override
        public List<? extends Profile> list(Tenant tenant) {
            return List.of(new ProfileRecord(SDELAMO.getName(), SDELAMO.getAttributes().get("email").toString(),
                    TimeZone.getDefault(),
                    DayOfWeek.MONDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(16, 30),
                    TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
                    Format.MARKDOWN,
                    null,
                    null,
                    true
            ));
        }

        @Override
        public Optional<? extends Profile> findById(String id, Tenant tenant) {
            return Optional.of(new ProfileRecord(SDELAMO.getName(), SDELAMO.getAttributes().get("email").toString(),
                    TimeZone.getDefault(),
                    DayOfWeek.MONDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(16, 30),
                    TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
                    Format.MARKDOWN,
                    null,
                    null,
                    true
            ));
        }
    }

    @Test
    void crud(@Client("/") HttpClient httpClient,
              AnswerRepository answerRepository,
              AuthenticationFetcherMock authenticationFetcher) {
        authenticationFetcher.setAuthentication(SDELAMO);
        BlockingHttpClient client = httpClient.toBlocking();
        String questionId = "xxx";

        // SAVE with errors
        URI saveUri = UriBuilder.of("/question").path("save").build();
        String savePayloadWithErrors = "title=&howOften=DAILY_ON&onceAWeekDay=MONDAY&everyOtherWeekDay=MONDAY&onceAMonthOnTheFirstDay=MONDAY&timeOfDay=END";
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(BrowserRequest.POST(saveUri, savePayloadWithErrors)))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .bodyHtmlTest(html -> html.contains("must not be blank")) // title missing
                .bodyHtmlTest(html -> html.contains("You must select at least one day")); // days missing when selecting DAILY_ON

        // UPDATE with errors
        URI updateUri = UriBuilder.of("/question").path(questionId).path("update").build();
        String updatePayloadWithErrors = "title=&howOften=DAILY_ON&onceAWeekDay=MONDAY&everyOtherWeekDay=MONDAY&onceAMonthOnTheFirstDay=MONDAY&timeOfDay=END";
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(BrowserRequest.POST(updateUri, updatePayloadWithErrors)))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .bodyHtmlTest(html -> html.contains("must not be blank")) // title missing
                .bodyHtmlTest(html -> html.contains("You must select at least one day")); // days missing when selecting DAILY_ON

        assertThat(client.exchange(BrowserRequest.GET("/question/list"), String.class))
                .satisfies(htmlPage())
                .satisfies(htmlBody(Pattern.compile("""
                Asking 1 person\\s*every weekday\\s*at the end of the day.""")));

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path(questionId).path("edit").build()), String.class))
                .satisfies(htmlPage())
                .satisfies(htmlBody("""
                <li class="breadcrumb-item"><a href="/question/list">"""));

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("yyy").path("edit").build()), String.class))
                .satisfies(redirection("/notFound"));

        answerRepository.save(new AnswerRecord("zzz", "yyy", SDELAMO.getName(), LocalDate.now(), Format.MARKDOWN, "This is *my* answer."), null);

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("yyy").path("show").build()), String.class))
                .satisfies(redirection("/notFound"));

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xxx").path("show").build()), String.class))
                .satisfies(htmlPage())
                .satisfies(htmlBody(Pattern.compile("""
                Asking 1 person\\s*every weekday\\s*at the end of the day.""")))
                .satisfies(htmlBody("Today"))
                .satisfies(htmlBody("This is <em>my</em> answer."))
                .satisfies(htmlBody("/question/yyy/answer/zzz/edit"))
                .satisfies(htmlBody("""
                <li class="breadcrumb-item"><a href="/question/list">"""));

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("create").build()), String.class))
                .satisfies(htmlPage())
                .satisfies(htmlBody("""
                <li class="breadcrumb-item"><a href="/question/list">"""))
                .satisfies(htmlBody(Pattern.compile("""
                <input type="text" class="form-control"\\s*id="title""")));
    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {
        @Override
        @NonNull
        public String save(@NotNull @Valid Question questionSave, @Nullable Tenant tenant) {
            return "xxx";
        }

        @Override
        @NonNull
        public Optional<QuestionRecord> findById(@NotBlank String id, @Nullable Tenant tenant) {
            if (id.equals("xxx")) {
                return Optional.of(new QuestionRecord("xxx", "What are working on?", HowOften.DAILY_ON, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END, LocalTime.of(16, 30), Set.of(new RespondentRecord("user1", now()))));
            }
            return Optional.empty();
        }

        @Override
        public void update(@NotNull @Valid Question questionUpdate, @Nullable Tenant tenant) {

        }

        @Override
        @NonNull
        public List<QuestionRecord> findAll(@Nullable Tenant tenant) {
            return findById("xxx", tenant).map(List::of).orElseGet(Collections::emptyList);
        }

        @Override
        public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {

        }
    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock implements AnswerRepository {

        Map<String, Answer> answers = new HashMap<>();

        @Override
        public String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
            final String id = "zzz";
            answers.put(id, answer);
            return id;
        }

        @Override
        public void update(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
            answers.put(answer.id(), answer);
        }

        @Override
        public Optional<? extends Answer> findById(@NotBlank String id, @Nullable Tenant tenant) {
            return Optional.ofNullable(answers.get(id));
        }

        @Override
        public List<? extends Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant) {
            return answers.values().stream().toList();
        }

        @Override
        public List<? extends Answer> findByQuestionIdAndRespondentId(String questionId, String respondentId) {
            return answers.values().stream().filter(a -> a.questionId().equals(questionId) && a.respondentId().equals(respondentId)).toList();
        }
    }
}
