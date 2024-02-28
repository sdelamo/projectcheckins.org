package org.projectcheckins.security.http;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

@MicronautTest
class SecurityControllerTest {

    @Test
    void login(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.retrieve(BrowserRequest.GET("/security/login")))
            .matches(html -> html.contains("""
    type="password"""));
    }
}
