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