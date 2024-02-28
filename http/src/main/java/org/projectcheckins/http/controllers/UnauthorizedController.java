package org.projectcheckins.http.controllers;

import org.projectcheckins.annotations.GetHtml;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.rules.SecurityRule;

import java.util.Collections;
import java.util.Map;

@Controller
class UnauthorizedController {

    @GetHtml(uri = "/unauthorized", view = "/error/401.html", rolesAllowed = SecurityRule.IS_ANONYMOUS)
    Map<String, Object> index() {
        return Collections.emptyMap();
    }
}
