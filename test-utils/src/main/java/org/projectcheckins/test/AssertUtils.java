package org.projectcheckins.test;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
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

    public static Consumer<HttpResponse<?>> status(HttpStatus httpStatus) {
        return response -> assertThat((CharSequence) response.getStatus()).isEqualTo(httpStatus);
    }

    public static Consumer<HttpResponse<?>> htmlBody(Consumer<String> consumer) {
        return response -> response.getBody(String.class).ifPresent(consumer::accept);
    }

    public static Consumer<HttpResponse<?>> htmlBody(String expected) {
        return htmlBody(html -> assertThat(html).contains(expected));
    }

    public static Consumer<HttpResponse<?>> htmlBody(Pattern regex) {
        return htmlBody(html -> assertThat(html).containsPattern(regex));
    }

    public static Consumer<HttpResponse<?>> htmlBody() {
        return htmlBody(html -> assertThat(html).contains("<!DOCTYPE html>"));
    }

    public static Consumer<HttpResponse<?>> htmlPage(HttpStatus httpStatus) {
        return status(httpStatus).andThen(htmlBody());
    }

    public static Consumer<HttpResponse<?>> htmlPage() {
        return htmlPage(HttpStatus.OK);
    }

    public static Consumer<HttpResponse<?>> location(Consumer<String> expected) {
        return response -> assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).hasValueSatisfying(expected);
    }

    public static Consumer<HttpResponse<?>> location(String location) {
        return location(location::equals);
    }

    public static Consumer<HttpResponse<?>> redirection(Consumer<String> expected) {
        return status(HttpStatus.SEE_OTHER).andThen(location(expected));
    }

    public static Consumer<HttpResponse<?>> redirection(String expected) {
        return status(HttpStatus.SEE_OTHER).andThen(location(expected));
    }

    public static Consumer<HttpResponse<?>> unauthorized() {
        return redirection("/unauthorized");
    }
}

