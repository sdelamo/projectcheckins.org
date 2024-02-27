package org.projectcheckins.http.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.http.AssertUtils;
import org.projectcheckins.http.BrowserRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "QuestionControllerTest")
@MicronautTest
class QuestionControllerTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<String>  listResponse = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET("/question/list"), String.class));
        AssertUtils.assertHtmlPage(listResponse);

        HttpResponse<String>  editResponse = assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xxx").path("edit").build()), String.class));
        AssertUtils.assertHtmlPage(editResponse);

        HttpResponse<String>  notFoundQuestionEditResponse = assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("yyy").path("edit").build()), String.class));
        AssertUtils.assertRedirection(notFoundQuestionEditResponse, "/notFound"::equals);

        HttpResponse<String>  showResponse = assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xxx").path("show").build()), String.class));
        AssertUtils.assertHtmlPage(showResponse);

        HttpResponse<String>  createResponse = assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("create").build()), String.class));
        String html = AssertUtils.assertHtmlPage(createResponse);
        assertTrue(html.contains("""
                <input type="text" name="title"""));

    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {
        @Override
        public String save(QuestionSave questionSave) {
            return "xxx";
        }

        @Override
        public Optional<Question> findById(String id) {
            if (id.equals("xxx")) {
                return Optional.of(new Question("xxx", "What are working on?"));
            }
            return Optional.empty();
        }

        @Override
        public void update(QuestionUpdate questionUpdate) {

        }

        @Override
        public List<Question> findAll() {
            return Collections.emptyList();
        }

        @Override
        public void deleteById(String id) {

        }
    }
}
