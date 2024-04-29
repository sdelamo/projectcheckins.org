package org.projectcheckins.assets;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@MicronautTest
class BootstrapTest {
    @Test
    void testBootstrap(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/stylesheets/bootstrap-5.3.2/bootstrap.css")));
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/stylesheets/bootstrap-5.3.2/bootstrap.css.map")));
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/stylesheets/bootstrap-5.3.2/bootstrap.min.css")));
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/stylesheets/bootstrap-5.3.2/bootstrap.min.css.map")));
    }
}
