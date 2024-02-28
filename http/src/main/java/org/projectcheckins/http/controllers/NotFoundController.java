package org.projectcheckins.http.controllers;

import org.projectcheckins.annotations.GetHtml;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.rules.SecurityRule;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

@Controller
class NotFoundController {
    private static final String PATH = "/notFound";
    public static final Supplier<HttpResponse<?>> NOT_FOUND_REDIRECT = () -> HttpResponse.seeOther(URI.create(PATH));

    @GetHtml(uri = PATH, view = "/error/404.html", rolesAllowed = SecurityRule.IS_ANONYMOUS)
    Map<String, Object> index() {
        return Collections.emptyMap();
    }
}
