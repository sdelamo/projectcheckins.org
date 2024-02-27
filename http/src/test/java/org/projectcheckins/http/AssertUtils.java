package org.projectcheckins.http;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public final class AssertUtils {
    private AssertUtils() {
    }

    public static String assertHtmlPage(HttpResponse<?> response, HttpStatus httpStatus) {
        assertEquals(httpStatus, response.getStatus());
        Optional<String> htmlOptional = response.getBody(String.class);
        assertTrue(htmlOptional.isPresent());
        String html = htmlOptional.get();
        assertTrue(html.contains("<!DOCTYPE html>"));
        return html;
    }

    public static String assertHtmlPage(HttpResponse<?> response) {
        return assertHtmlPage(response, HttpStatus.OK);
    }

    public static void assertUnauthorized(HttpResponse<?> rsp) {
        assertRedirection(rsp, s -> s.equals("/unauthorized"));
    }

    public static void assertRedirection(HttpResponse<?> rsp, Predicate<String> expected) {
        assertEquals(HttpStatus.SEE_OTHER, rsp.getStatus());
        String location= rsp.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(expected.test(location));
    }
}

