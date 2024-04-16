package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.endpoints.LoginControllerConfiguration;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Alert;
import org.projectcheckins.security.PasswordService;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
class SecurityController {
    private static final String SECURITY = "security";
    private static final String PATH = "/" + SECURITY;
    private static final String MODEL_FORM = "form";
    private static final String MODEL_PASSWORD_FORM = "passwordForm";
    private static final String MODEL_ALERT = "alert";

    // LOGIN
    private static final String ACTION_LOGIN = "login";
    private static final String VIEW_SECURITY_LOGIN = PATH + "/" + ACTION_LOGIN + ".html";
    private static final String PATH_LOGIN = PATH + "/" + ACTION_LOGIN;
    private static final URI URI_LOGIN = UriBuilder.of(PATH).path(ACTION_LOGIN).build();

    private final Form loginForm;

    // SIGN UP
    private final Form signUpForm;
    private static final String VIEW_SECURITY_SIGN_UP = PATH + "/signUp.html";
    private static final String PATH_SIGN_UP = PATH + "/signUp";

    // PASSWORD
    private static final String ACTION_PASSWORD_CHANGE = "changePassword";
    private static final String ACTION_PASSWORD_UPDATE = "updatePassword";
    private static final String PATH_PASSWORD_CHANGE = PATH + "/" + ACTION_PASSWORD_CHANGE;
    private static final String PATH_PASSWORD_UPDATE = PATH + "/" + ACTION_PASSWORD_UPDATE;
    private static final String VIEW_PASSWORD_CHANGE = PATH + "/" + ACTION_PASSWORD_CHANGE + ".html";
    private static final String VIEW_CHANGED_PASSWORD = PATH + "/passwordChanged.html";

    private final FormGenerator formGenerator;
    private final RegisterService registerService;
    private final PasswordService passwordService;

    SecurityController(FormGenerator formGenerator,
                       LoginControllerConfiguration loginControllerConfiguration,
                       RegisterService registerService,
                       PasswordService passwordService) {
        this.formGenerator = formGenerator;
        this.registerService = registerService;
        this.passwordService = passwordService;
        loginForm = formGenerator.generate(loginControllerConfiguration.getPath(), LoginForm.class);
        signUpForm = formGenerator.generate(PATH_SIGN_UP, SignUpForm.class);
    }

    @GetHtml(uri = PATH_SIGN_UP, rolesAllowed = SecurityRule.IS_ANONYMOUS, view = VIEW_SECURITY_SIGN_UP)
    Map<String, Object> signUpCreate() {
        return Collections.singletonMap(MODEL_FORM, signUpForm);
    }

    @PostForm(uri = PATH_SIGN_UP, rolesAllowed = SecurityRule.IS_ANONYMOUS)
    HttpResponse<?> signUp(@NonNull @NotNull @Valid @Body SignUpForm form) {
        try {
            registerService.register(form.email(), form.password());
        } catch (UserAlreadyExistsException e) {
            return HttpResponse.unprocessableEntity().body(new ModelAndView<>(VIEW_SECURITY_SIGN_UP,
                    Map.of(
                            MODEL_FORM, signUpForm,
                            MODEL_ALERT, Alert.danger(Message.of("User already exists", "user.already.exists")))));
        }
        return HttpResponse.seeOther(URI_LOGIN);
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

    @GetHtml(uri = PATH_PASSWORD_CHANGE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_PASSWORD_CHANGE)
    Map<String, Object> changePassword(@NonNull Authentication authentication) {
        final PasswordForm passwordForm = new PasswordForm(authentication);
        final Form form = formGenerator.generate(PATH_PASSWORD_UPDATE, passwordForm);
        return Map.of(MODEL_PASSWORD_FORM, form);
    }

    @PostForm(uri = PATH_PASSWORD_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> updatePassword(@NonNull @NotNull @Valid @Body PasswordForm form,
                                   @NonNull Authentication authentication) {
        final String userId = form.userId();
        if (userId.equals(authentication.getName())) {
            this.passwordService.updatePassword(userId, form.newPassword());
            return HttpResponse.ok().body(new ModelAndView<>(VIEW_CHANGED_PASSWORD, Collections.emptyMap()));
        }
        return HttpResponse.seeOther(URI_LOGIN);
    }

    @NonNull
    private static Message messageOf(@NonNull AuthenticationFailureReason reason) {
        return switch (reason) {
            case USER_DISABLED -> Message.of("User disabled. Verify your email address first.", "user.disabled");
            // don't give more information for security reasons.
            default -> Message.of("The username or password is incorrect. Please try again.", "login.failed");
        };
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request,
                                                          @Nullable Authentication authentication,
                                                          ConstraintViolationException ex) {
        if (request.getPath().equals(PATH_SIGN_UP)) {
            return request.getBody(SignUpForm.class)
                    .map(signUpForm -> HttpResponse.ok()
                            .body(new ModelAndView<>(VIEW_SECURITY_SIGN_UP,
                                    Collections.singletonMap(MODEL_FORM,
                                            formGenerator.generate(PATH_SIGN_UP, signUpForm, ex)))))
                    .orElseGet(HttpResponse::serverError);
        } else if (request.getPath().equals(PATH_PASSWORD_UPDATE)) {
            if (authentication == null) {
                return HttpResponse.seeOther(URI_LOGIN);
            }
            final PasswordForm passwordForm = new PasswordForm(authentication);
            final Form form = formGenerator.generate(PATH_PASSWORD_UPDATE, passwordForm, ex);
            final Map<String, Object> model = Collections.singletonMap(MODEL_PASSWORD_FORM, form);
            return HttpResponse.ok().body(new ModelAndView<>(VIEW_PASSWORD_CHANGE, model));
        }
        return HttpResponse.serverError();
    }
}
