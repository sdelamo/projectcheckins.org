package org.projectcheckins.http.controllers;

import io.micronaut.context.annotation.Property;
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
import org.projectcheckins.core.api.*;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.*;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;
import org.projectcheckins.test.HttpClientResponseExceptionAssert;

import java.net.URI;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.projectcheckins.test.AbstractAuthenticationFetcher.SDELAMO;
import static org.projectcheckins.test.AssertUtils.htmlBody;
import static org.projectcheckins.test.AssertUtils.htmlPage;

@MicronautTest
@Property(name = "spec.name", value = "AnswerControllerTest")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
class AnswerControllerTest {
    @Test
    void crud(@Client("/") HttpClient httpClient,
              AuthenticationFetcherMock authenticationFetcher,
              AnswerRepositoryMock answerRepositoryMock) {
        BlockingHttpClient client = httpClient.toBlocking();
        authenticationFetcher.setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        String questionId = "123";

        String markdown = """
                                    - 4.4.0 core release and building every module with 4.4.0 core.
                                    - HTMX integration
                                    - Project checkins
                        """;


        Map<String, Object> body = Map.of("questionId", questionId,
                "respondentId", AbstractAuthenticationFetcher.SDELAMO.getName(),
                "answerDate", "2024-03-11",
                "markdown", markdown);

        URI bogusMarkdownUri = UriBuilder.of("/question").path("bogus").path("answer").path("markdown").build();
        HttpRequest<?> bogusMarkdownRequest = BrowserRequest.POST(bogusMarkdownUri.toString(), body);
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(bogusMarkdownRequest))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY);


        URI markdownUri = UriBuilder.of("/question").path(questionId).path("answer").path("markdown").build();
        HttpRequest<?> request = BrowserRequest.POST(markdownUri.toString(), body);
        assertDoesNotThrow(() -> client.exchange(request));

        String html = """
                                    <ul>
                                    <li>4.4.0 core release and building every module with 4.4.0 core.</li>
                                    <li>HTMX integration</li>
                                    <li>Project checkins</li>
                                    </ul>
                        """;
        URI wysiwygUri = UriBuilder.of("/question").path(questionId).path("answer").path("wysiwyg").build();
        Map<String, Object> bodyWysiwyg = Map.of("questionId", questionId,
                "respondentId", AbstractAuthenticationFetcher.SDELAMO.getName(),
                "answerDate", "2024-03-12",
                "html", html);

        URI bogusWysiwygUri = UriBuilder.of("/question").path("bogus").path("answer").path("wysiwyg").build();
        HttpRequest<?> bogusWysiwygRequest = BrowserRequest.POST(bogusWysiwygUri.toString(), bodyWysiwyg);
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(bogusWysiwygRequest))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        HttpRequest<?> wysiwygRequest = BrowserRequest.POST(wysiwygUri.toString(), bodyWysiwyg);
        assertDoesNotThrow(() -> client.exchange(wysiwygRequest));
        LocalDate expectedMarkdownAnswerDate = LocalDate.of(2024, 3, 11);
        LocalDate expectedWysiwygAnswerDate = LocalDate.of(2024, 3, 12);
        assertThat(answerRepositoryMock.getAnswers())
                .anyMatch(a -> a.format() == Format.MARKDOWN && a.text().equals(markdown) && a.answerDate().equals(expectedMarkdownAnswerDate))
                .anyMatch(a -> a.format() == Format.WYSIWYG && a.text().equals(html) && a.answerDate().equals(expectedWysiwygAnswerDate));

        Assertions.assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path(questionId).path("answer").path("any-answer").path("show").build()), String.class))
                .matches(htmlPage())
                .matches(htmlBody("""
                        <div class="date">"""))
                .matches(htmlBody("""
                        <div class="respondent">"""))
                .matches(htmlBody("<span>Code Monkey</span>"))
                .matches(htmlBody("""
                        <a href="/question/list">"""))
                .matches(htmlBody("""
                        <a href="/question/123/show">"""))
                .matches(htmlBody(Pattern.compile("""
                        <a href="/question/123/answer/.*/show">""")))
                .matches(htmlBody(Pattern.compile("""
                        <a href="/question/123/answer/.*/edit">""")));
    }

    @Test
    void markdownMustNotBeBlank(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final String questionId = "123";
        final Map<String, Object> body = Map.of("questionId", questionId, "respondentId", SDELAMO.getName(), "answerDate", "1970-01-01");
        final URI uri = UriBuilder.of("/question").path(questionId).path("answer").path("markdown").build();
        final HttpRequest<Map<String, Object>> request = BrowserRequest.POST(uri.toString(), body);
        authenticationFetcher.setAuthentication(SDELAMO);
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(request))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .bodyHtmlContains("""
                        <div id="markdownValidationServerFeedback" class="invalid-feedback">must not be blank</div>""");
    }

    @Test
    void wysiwygMustNotBeBlank(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcher) {
        final BlockingHttpClient client = httpClient.toBlocking();
        final String questionId = "123";
        final Map<String, Object> body = Map.of("questionId", questionId, "respondentId", SDELAMO.getName(), "answerDate", "1970-01-01");
        final URI uri = UriBuilder.of("/question").path(questionId).path("answer").path("wysiwyg").build();
        final HttpRequest<Map<String, Object>> request = BrowserRequest.POST(uri.toString(), body);
        authenticationFetcher.setAuthentication(SDELAMO);
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(request))
                .hasStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .bodyHtmlContains("""
                        <div id="htmlValidationServerFeedback" class="invalid-feedback">must not be blank</div>""");
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    static class AnswerRepositoryMock extends SecondaryAnswerRepository {

        private List<Answer> answers = new ArrayList<>();
        @Override
        @NonNull
        public String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant) {
            final String id = "xxx";
            this.answers.add(answer);
            return id;
        }

        public List<Answer> getAnswers() {
            return answers;
        }

        @Override
        public Optional<? extends Answer> findById(@NotBlank String id, @Nullable Tenant tenant) {
            return answers.stream().findAny();
        }

        @Override
        @NonNull
        public List<? extends Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant) {
            return Collections.emptyList();
        }

        @Override
        public List<? extends Answer> findByQuestionIdAndRespondentId(String questionId, String respondentId) {
            return answers.stream().filter(a -> a.questionId().equals(questionId) && a.respondentId().equals(respondentId)).toList();
        }
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    static class QuestionRepositoryMock extends SecondaryQuestionRepository {
        @Override
        public Optional<? extends Question> findById(String id, Tenant tenant) {
            return Optional.of(new QuestionRecord(
                    "123",
                    "title",
                    HowOften.DAILY_ON,
                    Set.of(DayOfWeek.MONDAY),
                    TimeOfDay.FIXED,
                    LocalTime.of(16, 30),
                    Collections.emptySet()
            ));
        }
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    static class ProfileRepositoryMock extends SecondaryProfileRepository {

        @Override
        public List<? extends Profile> list(Tenant tenant) {
            return Collections.emptyList();
        }

        @Override
        public Optional<? extends Profile> findById(String id, Tenant tenant) {
            return Optional.of(new ProfileRecord(
                    id,
                    id + "@example.com",
                    TimeZone.getDefault(),
                    DayOfWeek.MONDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(16, 30),
                    TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
                    Format.MARKDOWN,
                    "Code",
                    "Monkey"
            ));
        }
    }

}