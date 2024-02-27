package org.projectcheckins.http.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        HttpResponse<?> saveResponse = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.POST("/question/save", Map.of("title", "What are working on?"))));
        assertRedirection(saveResponse, s -> s.startsWith("/question") && s.endsWith("/show"));

        URI updateUri = UriBuilder.of("/question").path("yyy").path("update").build();
        HttpResponse<?> updateResponse = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of("id", "yyy", "title", "What are working on?"))));
        assertRedirection(updateResponse, "/question/yyy/show"::equals);

        HttpClientResponseException thrown = Assertions.assertThrows(HttpClientResponseException.class, () -> client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of("id", "xxx", "title", "What are working on?"))));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.getStatus());

        URI deleteUri = UriBuilder.of("/question").path("yyy").path("delete").build();
        HttpResponse<?> deleteResponse = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.POST(deleteUri.toString(), Map.of("title", "What are working on?"))));
        assertRedirection(deleteResponse, "/question/list"::equals);
    }

    @Requires(property = "spec.name", value = "QuestionControllerFormTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {
        @Override
        public String save(QuestionSave questionSave) {
            return "xxx";
        }

        @Override
        public Optional<Question> findById(String id) {
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
