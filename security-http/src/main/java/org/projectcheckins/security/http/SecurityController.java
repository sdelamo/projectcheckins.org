package org.projectcheckins.security.http;

import org.projectcheckins.annotations.GetHtml;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.endpoints.LoginControllerConfiguration;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;

import java.util.Map;

@Controller
class SecurityController {
    private static final String MODEL_FORM = "form";
    private static final String VIEW_SECURITY_LOGIN = "/security/login.html";
    private static final String PATH_LOGIN = "/security/login";
    private final Form loginForm;

    SecurityController(FormGenerator formGenerator, 
                       LoginControllerConfiguration loginControllerConfiguration) {
         loginForm = formGenerator.generate(loginControllerConfiguration.getPath(), LoginForm.class);
    }

    @GetHtml(uri = PATH_LOGIN, rolesAllowed = SecurityRule.IS_ANONYMOUS, view = VIEW_SECURITY_LOGIN)
    Map<String, Object> login() {
        return Map.of(MODEL_FORM, loginForm);
    }
}
