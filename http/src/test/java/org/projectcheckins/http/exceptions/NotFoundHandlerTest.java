package org.projectcheckins.http.exceptions;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.test.AssertUtils.htmlBody;

import org.projectcheckins.annotations.GetHtml;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.exceptions.QuestionNotFoundException;
import org.projectcheckins.test.BrowserRequest;

import static org.projectcheckins.test.AssertUtils.status;

@MicronautTest
@Property(name = "spec.name", value = "NotFoundHandlerTest")
class NotFoundHandlerTest {

    @Test
    void notFoundJson(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/notFound/throwing");
        assertThatThrownBy(() -> client.exchange(request, String.class))
            .isInstanceOf(HttpClientResponseException.class)
            .extracting(e -> ((HttpClientResponseException)e).getStatus())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void notFoundHtml(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = BrowserRequest.GET("/notFound/throwing/html");
        Argument<String> ok = Argument.of(String.class);
        Argument<String> ko = Argument.of(String.class);
        assertThatThrownBy(() -> client.exchange(request, ok, ko))
            .isInstanceOf(HttpClientResponseException.class)
            .extracting(e -> ((HttpClientResponseException)e).getResponse())
            .matches(status(HttpStatus.NOT_FOUND))
            .matches(htmlBody("Not Found"));
    }

    @Controller
    @Requires(property = "spec.name", value = "NotFoundHandlerTest")
    static class NotFoundController {
        @Secured(SecurityRule.IS_ANONYMOUS)
        @Get(uri = "/notFound/throwing")
        HttpResponse<?> index() {
            throw new QuestionNotFoundException();
        }

        @GetHtml(uri = "/notFound/throwing/html", rolesAllowed = SecurityRule.IS_ANONYMOUS)
        HttpResponse<?> html() {
            throw new QuestionNotFoundException();
        }

    }
}
