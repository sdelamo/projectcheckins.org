package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.test.BrowserRequest;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@MicronautTest
class HomeControllerTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/")))
            .satisfies(redirection("/question/list"));
    }
}