package org.projectcheckins.http.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.http.BrowserRequest;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.projectcheckins.http.AssertUtils.assertRedirection;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "QuestionControllerFormTest")
@MicronautTest
class QuestionControllerFormTest {
    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String title = "What are working on?";
        HttpResponse<?> saveResponse = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.POST("/question/save", Map.of("title", title))));
        assertRedirection(saveResponse, s -> s.startsWith("/question") && s.endsWith("/show"));
        String location = saveResponse.getHeaders().get(HttpHeaders.LOCATION);
        String id = location.substring(location.indexOf("/question/") + "/question/".length(), location.lastIndexOf("/show"));

        String html = Assertions.assertDoesNotThrow(() -> client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path("list").build()), String.class));
        assertTrue(html.contains(title));
        assertTrue(html.contains("/question/create"));

        html = Assertions.assertDoesNotThrow(() -> client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path(id).path("show").build()), String.class));
        assertTrue(html.contains(title));
        assertTrue(html.contains("/question/list"));
        assertTrue(html.contains("/question/" + id  + "/edit"));

        html = Assertions.assertDoesNotThrow(() -> client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path(id).path("edit").build()), String.class));
        assertTrue(html.contains(title));

        String updatedTitle = "What did you do today?";
        URI updateUri = UriBuilder.of("/question").path(id).path("update").build();
        HttpResponse<?> updateResponse = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of("id", id, "title", updatedTitle))));
        assertRedirection(updateResponse, s -> s.equals("/question/" + id + "/show"));

        html = Assertions.assertDoesNotThrow(() -> client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path(id).path("edit").build()), String.class));
        assertFalse(html.contains(title));
        assertTrue(html.contains(updatedTitle));

        HttpClientResponseException thrown = Assertions.assertThrows(HttpClientResponseException.class, () -> client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of("id", "yyy", "title", "What are working on?"))));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.getStatus());

        URI deleteUri = UriBuilder.of("/question").path("yyy").path("delete").build();
        HttpResponse<?> deleteResponse = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.POST(deleteUri.toString(), Map.of("title", "What are working on?"))));
        assertRedirection(deleteResponse, "/question/list"::equals);
    }

    @Requires(property = "spec.name", value = "QuestionControllerFormTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {

        Map<String, String> titleById = new HashMap<>();
        @Override
        public String save(QuestionSave questionSave) {
            String id = "xxx";
            titleById.put(id, questionSave.title());
            return id;
        }

        @Override
        public Optional<Question> findById(String id) {
            return Optional.ofNullable(titleById.get(id)).map(title -> new Question(id, titleById.get(id)));
        }

        @Override
        public void update(QuestionUpdate questionUpdate) {
            if (titleById.containsKey(questionUpdate.id())) {
                titleById.put(questionUpdate.id(), questionUpdate.title());
            }
        }

        @Override
        public List<Question> findAll() {
            return titleById.keySet().stream().map(id -> new Question(id, titleById.get(id))).toList();
        }

        @Override
        public void deleteById(String id) {
            titleById.remove(id);
        }
    }
}
