package org.projectcheckins.http.controllers;

import io.micronaut.http.MutableHttpResponse;
import org.projectcheckins.annotations.GetHtml;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.rules.SecurityRule;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Controller
class NotFoundController {
    private static final String PATH = "/notFound";

    @GetHtml(uri = PATH, view = "/error/404.html", rolesAllowed = SecurityRule.IS_ANONYMOUS)
    Map<String, Object> index() {
        return Collections.emptyMap();
    }


    public static <T> MutableHttpResponse<T> notFoundRedirect() {
        return HttpResponse.seeOther(URI.create(PATH));
    }
}
