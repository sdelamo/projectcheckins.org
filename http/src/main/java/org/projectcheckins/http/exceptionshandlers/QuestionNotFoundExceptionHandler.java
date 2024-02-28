package org.projectcheckins.http.exceptionshandlers;


import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import io.micronaut.views.ViewsRenderer;
import jakarta.inject.Singleton;
import java.util.Map;
import org.projectcheckins.core.exceptions.QuestionNotFoundException;

import java.util.Collections;

@Produces
@Singleton
public class QuestionNotFoundExceptionHandler implements ExceptionHandler<QuestionNotFoundException, HttpResponse<?>> {

    private final ErrorResponseProcessor<?> errorResponseProcessor;
    private final ViewsRenderer<Map<?, ?>, HttpRequest<?>> viewsRenderer;

    public QuestionNotFoundExceptionHandler(ErrorResponseProcessor<?> errorResponseProcessor,
                                            ViewsRenderer<Map<?, ?>, HttpRequest<?>> viewsRenderer) {
        this.errorResponseProcessor = errorResponseProcessor;
        this.viewsRenderer = viewsRenderer;
    }

    @Override
    public HttpResponse<?> handle(@SuppressWarnings("rawtypes") HttpRequest request, QuestionNotFoundException e) {
        if (acceptHtml(request)) {
            Writable writable = viewsRenderer.render("error/404", Collections.emptyMap(), request);
            return HttpResponse.notFound(writable)
                    .contentType(MediaType.TEXT_HTML_TYPE);
        }
        return errorResponseProcessor.processResponse(ErrorContext.builder(request)
                .cause(e)
                .errorMessage("Not found")
                .build(), HttpResponse.notFound());
    }

    private boolean acceptHtml(HttpRequest<?> request) {
        return request.getHeaders()
                .accept()
                .stream()
                .anyMatch(mediaType -> mediaType.getName().contains(MediaType.TEXT_HTML));
    }
}