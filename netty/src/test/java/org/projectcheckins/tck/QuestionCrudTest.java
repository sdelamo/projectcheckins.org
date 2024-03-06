package org.projectcheckins.tck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.test.BrowserRequest;
import java.util.Map;
import java.util.Optional;

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@MicronautTest
class QuestionCrudTest {

    @Test
    void questionCrud(@Client("/") HttpClient httpClient,
                      QuestionRepository questionRepository) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = BrowserRequest.POST("/question/save", Map.of("title", "What are working on?"));
        assertThatCode(() -> client.exchange(request))
            .doesNotThrowAnyException();
        Optional<Question> questionOptional = questionRepository.findAll()
                .stream()
                .filter(q -> q.title().equals("What are working on?"))
                .findFirst();
        assertThat(questionOptional).isNotEmpty();
        questionOptional.map(Question::id).ifPresent(questionRepository::deleteById);
    }
}
