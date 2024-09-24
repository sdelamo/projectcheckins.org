// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.http.exceptions;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.test.AssertUtils.htmlBody;

import io.micronaut.http.MediaType;
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
import org.projectcheckins.test.HttpClientResponseExceptionAssert;

import static org.projectcheckins.test.AssertUtils.status;
import static org.projectcheckins.test.HttpClientResponseExceptionAssert.*;

@MicronautTest
@Property(name = "spec.name", value = "NotFoundHandlerTest")
class NotFoundHandlerTest {

    @Test
    void notFoundJson(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/notFound/throwing");
        assertThatThrowsHttpClientResponseException(() -> client.exchange(request, String.class))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void notFoundHtml(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = BrowserRequest.GET("/notFound/throwing/html").accept(MediaType.TEXT_HTML);
        Argument<String> ok = Argument.of(String.class);
        Argument<String> ko = Argument.of(String.class);
        HttpClientResponseExceptionAssert.assertThatThrowsHttpClientResponseException(() -> client.exchange(request, ok, ko))
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyHtmlContains("Not Found");
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
