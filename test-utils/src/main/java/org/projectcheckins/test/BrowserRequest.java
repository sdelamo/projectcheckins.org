package org.projectcheckins.test;

import io.micronaut.http.HttpRequestFactory;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;

import java.net.URI;
import java.util.Objects;

public final class BrowserRequest {

    private BrowserRequest() {
    }

    public static <T> MutableHttpRequest<T> POST(String uri, T body) {
        Objects.requireNonNull(uri, "Argument [uri] is required");
        return HttpRequestFactory.INSTANCE.post(uri, body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_HTML);
    }

    public static MutableHttpRequest<?> GET(String uri) {
        return HttpRequestFactory.INSTANCE.get(uri).accept(MediaType.TEXT_HTML);
    }

    public static MutableHttpRequest<?> GET(URI uri) {
        return GET(uri.toString());
    }

}
