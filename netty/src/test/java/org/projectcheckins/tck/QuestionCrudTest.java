package org.projectcheckins.tck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.AbstractAuthenticationFetcher.SDELAMO;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@MicronautTest
@Property(name = "spec.name", value = "QuestionCrudTest")
class QuestionCrudTest {
    @Requires(property = "spec.name", value = "QuestionCrudTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
    }

    @Test
    void questionCrud(@Client("/") HttpClient httpClient,
                      QuestionRepository questionRepository,
                      RegisterService registerService,
                      AuthenticationFetcherMock authenticationFetcher) throws UserAlreadyExistsException {
        String email = "delamos@unityfoundation.io";
        String userId = registerService.register(email, "secret");
        Authentication authentication = Authentication.build(userId, Collections.emptyList(), Collections.singletonMap("email", email));
        authenticationFetcher.setAuthentication(authentication);
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = BrowserRequest.POST("/question/save", Map.of(
                "title", "What are working on?",
                "schedule", "schedule",
                "timeZone", TimeZone.getDefault().getID()));
        assertThatCode(() -> client.exchange(request))
            .doesNotThrowAnyException();
        Optional<? extends Question> questionOptional = questionRepository.findAll()
                .stream()
                .filter(q -> q.title().equals("What are working on?"))
                .findFirst();
        assertThat(questionOptional).isNotEmpty();
        questionOptional.map(Question::id).ifPresent(questionRepository::deleteById);
    }
}
