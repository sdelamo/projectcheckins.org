package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.unauthorized;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.projectcheckins.test.BrowserRequest;

@Property(name = "micronaut.security.redirect.unauthorized.url", value = "/unauthorized")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@MicronautTest
class ProfileControllerSecurityTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/profile/show")))
            .matches(unauthorized());

        assertThat(client.exchange(BrowserRequest.GET("/profile/edit")))
            .matches(unauthorized());

        assertThat(client.exchange(BrowserRequest.POST("/profile/update", Map.of(
            "timeZone", TimeZone.getDefault().getID(),
            "firstDayOfWeek", DayOfWeek.MONDAY.name(),
            "using24HourClock", true))))
            .matches(unauthorized());
    }

}
