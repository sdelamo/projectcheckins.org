package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.htmlBody;
import static org.projectcheckins.test.AssertUtils.htmlPage;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.test.BrowserRequest;

@Property(name = "micronaut.server.locale-resolution.fixed", value = "en")
@MicronautTest
class NotFoundControllerTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/notFound"), String.class))
            .matches(htmlPage())
            .matches(htmlBody("Not Found"));
    }
}