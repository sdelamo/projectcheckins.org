package org.projectcheckins.http.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.net.URI;

@Controller
class HomeController {

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get
    HttpResponse<?> index() {
        return HttpResponse.seeOther(URI.create(QuestionController.PATH_LIST));
    }
}
