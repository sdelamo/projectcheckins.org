package org.projectcheckins.http.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.fields.messages.Message;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.bootstrap.Breadcrumb;

import java.net.URI;

@Controller
class HomeController {
    private static final Message MESSAGE_HOME = Message.of("Home", "home");
    public static final Breadcrumb BREADCRUMB_HOME = new Breadcrumb(MESSAGE_HOME, "/");

    @GetHtml(uri = "/", rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> index() {
        return HttpResponse.seeOther(URI.create(QuestionController.PATH_LIST));
    }
}
