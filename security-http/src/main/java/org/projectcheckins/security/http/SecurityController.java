package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.views.fields.messages.Message;
import org.projectcheckins.annotations.GetHtml;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.endpoints.LoginControllerConfiguration;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import org.projectcheckins.bootstrap.Alert;

import java.util.HashMap;
import java.util.Map;

@Controller
class SecurityController {
    private static final String MODEL_FORM = "form";
    private static final String MODEL_ALERT = "alert";
    private static final String VIEW_SECURITY_LOGIN = "/security/login.html";
    private static final String PATH_LOGIN = "/security/login";
    private final Form loginForm;

    SecurityController(FormGenerator formGenerator, 
                       LoginControllerConfiguration loginControllerConfiguration) {
         loginForm = formGenerator.generate(loginControllerConfiguration.getPath(), LoginForm.class);
    }

    @GetHtml(uri = PATH_LOGIN, rolesAllowed = SecurityRule.IS_ANONYMOUS, view = VIEW_SECURITY_LOGIN)
    Map<String, Object> login(@Nullable @QueryValue AuthenticationFailureReason reason) {
        Map<String, Object> model = new HashMap<>();
        model.put(MODEL_FORM, loginForm);
        if (reason != null) {
            model.put(MODEL_ALERT, Alert.danger(messageOf(reason)));
        }
        return model;
    }

    @NonNull
    private static Message messageOf(@NonNull AuthenticationFailureReason reason) {
        return switch (reason) {
            case USER_DISABLED -> Message.of("User disabled. Verify your email address first.", "user.disabled");
            // don't give more information for security reasons.
            default -> Message.of("The username or password is incorrect. Please try again.", "login.failed");
        };
    }
}
