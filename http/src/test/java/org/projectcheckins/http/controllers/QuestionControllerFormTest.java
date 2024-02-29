package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.http.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.http.BrowserRequest;

import java.net.URI;
import java.util.*;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "QuestionControllerFormTest")
@MicronautTest
class QuestionControllerFormTest {
    @Test
    void crud(@Client("/") HttpClient httpClient, QuestionRepository questionRepository) {
        BlockingHttpClient client = httpClient.toBlocking();
        String title = "What are working on?";
        HttpResponse<?> saveResponse = client.exchange(BrowserRequest.POST("/question/save", Map.of("title", title)));
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
        assertThat(client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of("id", id, "title", updatedTitle))))
            .matches(redirection(s -> s.equals("/question/" + id + "/show")));

        assertThat(client.retrieve(BrowserRequest.GET(UriBuilder.of("/question").path(id).path("edit").build()), String.class))
            .doesNotContain(title)
            .contains(updatedTitle);

        assertThatThrownBy(() -> client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of("id", "yyy", "title", "What are working on?"))))
            .isInstanceOf(HttpClientResponseException.class)
            .extracting(e -> ((HttpClientResponseException)e).getStatus())
            .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        URI deleteUri = UriBuilder.of("/question").path(id).path("delete").build();
        assertThat(client.exchange(BrowserRequest.POST(deleteUri.toString(), Collections.emptyMap())))
            .matches(redirection("/question/list"));

        assertThat(questionRepository.findAll()).isEmpty();
    }

    @Requires(property = "spec.name", value = "QuestionControllerFormTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {

        Map<String, String> titleById = new HashMap<>();

        @Override
        @NonNull
        public String save(@NotNull @Valid QuestionSave questionSave, @Nullable Tenant tenant) {
            String id = "xxx";
            titleById.put(id, questionSave.title());
            return id;
        }

        @Override
        @NonNull
        public Optional<Question> findById(@NotBlank String id, @Nullable Tenant tenant) {
            return Optional.ofNullable(titleById.get(id)).map(title -> new Question(id, titleById.get(id)));
        }

        @Override
        public void update(@NotNull @Valid QuestionUpdate questionUpdate, @Nullable Tenant tenant) {
            if (titleById.containsKey(questionUpdate.id())) {
                titleById.put(questionUpdate.id(), questionUpdate.title());
            }
        }

        @Override
        @NonNull
        public List<Question> findAll(@Nullable Tenant tenant) {
            return titleById.keySet().stream().map(id -> new Question(id, titleById.get(id))).toList();
        }

        @Override
        public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
            titleById.remove(id);
        }
    }
}
