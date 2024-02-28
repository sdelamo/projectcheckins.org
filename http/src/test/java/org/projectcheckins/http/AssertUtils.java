package org.projectcheckins.http;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;

import java.util.function.Predicate;

public final class AssertUtils {
    private AssertUtils() {
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
        return location(x -> location.equals(x));
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

