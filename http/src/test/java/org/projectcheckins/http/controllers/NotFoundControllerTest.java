package org.projectcheckins.http.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projectcheckins.http.AssertUtils;
import org.projectcheckins.http.BrowserRequest;

@Property(name = "micronaut.server.locale-resolution.fixed", value = "en")
@MicronautTest
class NotFoundControllerTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<String> response = Assertions.assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET("/notFound"), String.class));
        String html = AssertUtils.assertHtmlPage(response);
        Assertions.assertTrue(html.contains("Not Found"));
    }
}