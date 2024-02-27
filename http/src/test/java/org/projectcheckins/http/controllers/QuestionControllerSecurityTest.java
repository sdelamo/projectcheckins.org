package org.projectcheckins.http.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.http.AssertUtils;
import org.projectcheckins.http.BrowserRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.security.redirect.unauthorized.url", value = "/unauthorized")
@Property(name = "micronaut.security.authentication", value = "bearer")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@MicronautTest
class QuestionControllerSecurityTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> rsp = assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET("/question/list")));
        AssertUtils.assertUnauthorized(rsp);

        rsp = assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xx").path("edit").build())));
        AssertUtils.assertUnauthorized(rsp);

        rsp = assertDoesNotThrow(() -> client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("create").build())));
        AssertUtils.assertUnauthorized(rsp);
    }

}
