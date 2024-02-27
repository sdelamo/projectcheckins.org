package org.projectcheckins.http.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.net.URI;

@Controller
class HomeController {

    @Get
    HttpResponse<?> index() {
        return HttpResponse.seeOther(URI.create(QuestionController.PATH_LIST));
    }
}
