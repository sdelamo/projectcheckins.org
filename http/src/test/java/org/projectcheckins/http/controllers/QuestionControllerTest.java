package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.htmlPage;
import static org.projectcheckins.test.AssertUtils.htmlBody;
import static org.projectcheckins.test.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
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
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.test.BrowserRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "QuestionControllerTest")
@MicronautTest
class QuestionControllerTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/question/list"), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xxx").path("edit").build()), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("yyy").path("edit").build()), String.class))
            .matches(redirection("/notFound"));

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xxx").path("show").build()), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("create").build()), String.class))
            .matches(htmlPage())
            .matches(htmlBody("""
                <input type="text" name="title"""));

    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {
        @Override
        @NonNull
        public String save(@NotNull @Valid QuestionSave questionSave, @Nullable Tenant tenant) {
            return "xxx";
        }

        @Override
        @NonNull
        public Optional<Question> findById(@NotBlank String id, @Nullable Tenant tenant) {
            if (id.equals("xxx")) {
                return Optional.of(new Question("xxx", "What are working on?", "schedule"));
            }
            return Optional.empty();
        }

        @Override
        public void update(@NotNull @Valid QuestionUpdate questionUpdate, @Nullable Tenant tenant) {

        }

        @Override
        @NonNull
        public List<Question> findAll(@Nullable Tenant tenant) {
            return Collections.emptyList();
        }

        @Override
        public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {

        }
    }
}
