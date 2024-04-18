package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.unauthorized;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.test.BrowserRequest;

@Property(name = "micronaut.security.redirect.unauthorized.url", value = "/unauthorized")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@MicronautTest
class QuestionControllerSecurityTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/question/list")))
            .satisfies(unauthorized());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xx").path("edit").build())))
            .satisfies(unauthorized());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("create").build())))
            .satisfies(unauthorized());
    }

}
