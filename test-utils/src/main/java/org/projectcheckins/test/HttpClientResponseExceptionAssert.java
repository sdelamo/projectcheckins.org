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

package org.projectcheckins.test;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ThrowableAssert;
import java.util.function.Consumer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HttpClientResponseExceptionAssert extends AbstractAssert<HttpClientResponseExceptionAssert, AbstractObjectAssert<?, HttpClientResponseException>> {

    private HttpClientResponseExceptionAssert(AbstractObjectAssert<?, HttpClientResponseException> ex) {
        super(ex, AbstractAssert.class);
    }

    public static HttpClientResponseExceptionAssert assertThatThrowsHttpClientResponseException(ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        return new HttpClientResponseExceptionAssert(assertThatThrownBy(shouldRaiseThrowable)
                .isInstanceOf(HttpClientResponseException.class)
                .extracting(e -> (HttpClientResponseException) e));
    }

    public HttpClientResponseExceptionAssert hasStatus(HttpStatus status) {
        actual.extracting(HttpClientResponseException::getStatus).isEqualTo(status);
        return this;
    }

    public HttpClientResponseExceptionAssert bodyHtmlContains(String expected) {
        return bodyHtmlTest(html -> assertThat(html).contains(expected));
    }

    public HttpClientResponseExceptionAssert bodyHtmlTest(Consumer<String> expected) {
                    actual.extracting(HttpClientResponseException::getResponse)
                                    .satisfies(htmlBody(expected));
        return this;
    }

    public HttpClientResponseExceptionAssert doesNotHaveHeader(String headerName) {
        actual.extracting(e -> e.getResponse().getHeaders().get(headerName))
                .isNull();
        return this;
    }

    private static Consumer<HttpResponse<?>> htmlBody(Consumer<String> expected) {
        return response -> assertThat(response.getBody(String.class))
                              .hasValueSatisfying(expected);
    }
}
