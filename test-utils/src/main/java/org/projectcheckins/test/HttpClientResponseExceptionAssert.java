package org.projectcheckins.test;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ThrowableAssert;
import java.util.function.Predicate;
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
        return bodyHtmlTest(html -> html.contains(expected));
    }

    public HttpClientResponseExceptionAssert bodyHtmlTest(Predicate<String> expected) {
                    actual.extracting(HttpClientResponseException::getResponse)
                                    .matches(htmlBody(expected));
        return this;
    }

    public HttpClientResponseExceptionAssert doesNotHaveHeader(String headerName) {
        actual.extracting(e -> e.getResponse().getHeaders().get(headerName))
                .isNull();
        return this;
    }

    private static Predicate<HttpResponse<?>> htmlBody(Predicate<String> expected) {
        return response -> response.getBody(String.class)
                .filter(expected)
                .isPresent();
    }
}
