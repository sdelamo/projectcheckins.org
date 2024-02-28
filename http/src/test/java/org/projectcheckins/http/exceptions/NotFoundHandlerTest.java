package org.projectcheckins.http.exceptions;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.exceptions.QuestionNotFoundException;
import org.projectcheckins.http.AssertUtils;
import org.projectcheckins.http.BrowserRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@Property(name = "spec.name", value = "NotFoundHandlerTest")
class NotFoundHandlerTest {

    @Test
    void notFoundJson(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/notFound/throwing");
        HttpClientResponseException ex = Assertions.assertThrows(HttpClientResponseException.class, () -> client.exchange(request, String.class));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void notFoundHtml(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = BrowserRequest.GET("/notFound/throwing/html");
        Argument<String> ok = Argument.of(String.class);
        Argument<String> ko = Argument.of(String.class);
        HttpClientResponseException ex = Assertions.assertThrows(HttpClientResponseException.class, () -> client.exchange(request, ok, ko));
        String html = AssertUtils.assertHtmlPage(ex.getResponse(), HttpStatus.NOT_FOUND);
        Assertions.assertTrue(html.contains("Not Found"));
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
