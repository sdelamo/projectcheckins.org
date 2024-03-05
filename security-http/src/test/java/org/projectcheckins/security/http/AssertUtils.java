package org.projectcheckins.security.http;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert;

import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class AssertUtils {
    private AssertUtils() {
    }

    public static AbstractObjectAssert<?, String> assertThrowsWithHtml(ThrowableAssert.ThrowingCallable shouldRaiseThrowable, HttpStatus httpStatus) {
        return assertThatThrownBy(shouldRaiseThrowable)
                .isInstanceOf(HttpClientResponseException.class)
                .hasFieldOrPropertyWithValue("status", httpStatus)
                .extracting(e -> ((HttpClientResponseException) e).getResponse())
                .extracting(r -> r.getBody(String.class))
                .extracting(Optional::get);
    }

    public static Condition<String> containsOnlyOnce(String needle) {
        return containsManyTimes(1, needle);
    }

    public static Condition<String> containsManyTimes(int expectedTimes, String needle) {
        return new Condition<>(haystack -> haystack.split(needle, -1).length - 1 == expectedTimes,
                "Contains [%s] exactly %s times", needle, expectedTimes);
    }

    public static Predicate<HttpResponse<?>> status(HttpStatus httpStatus) {
        return response -> httpStatus.equals(response.getStatus());
    }

    public static Predicate<HttpResponse<?>> htmlBody(Predicate<String> expected) {
        return response -> response.getBody(String.class)
            .filter(expected)
            .isPresent();
    }

    public static Predicate<HttpResponse<?>> htmlBody(String expected) {
        return htmlBody(html -> html.contains("<!DOCTYPE html>"));
    }

    public static Predicate<HttpResponse<?>> htmlBody() {
        return htmlBody("<!DOCTYPE html>");
    }

    public static Predicate<HttpResponse<?>> htmlPage(HttpStatus httpStatus) {
        return status(httpStatus).and(htmlBody());
    }

    public static Predicate<HttpResponse<?>> htmlPage() {
        return htmlPage(HttpStatus.OK);
    }

    public static Predicate<HttpResponse<?>> location(Predicate<String> expected) {
        return response -> response.getHeaders().getFirst(HttpHeaders.LOCATION).filter(expected).isPresent();
    }

    public static Predicate<HttpResponse<?>> location(String location) {
        return location(location::equals);
    }

    public static Predicate<HttpResponse<?>> redirection(Predicate<String> expected) {
        return status(HttpStatus.SEE_OTHER).and(location(expected));
    }

    public static Predicate<HttpResponse<?>> redirection(String expected) {
        return status(HttpStatus.SEE_OTHER).and(location(expected));
    }

    public static Predicate<HttpResponse<?>> unauthorized() {
        return redirection("/unauthorized");
    }
}

