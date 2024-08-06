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

package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.http.server.util.locale.HttpLocaleResolver;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.endpoints.LoginControllerConfiguration;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.elements.InputSubmitFormElement;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Alert;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.security.PasswordService;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.RegistrationCheckViolationException;
import org.projectcheckins.security.constraints.ValidToken;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
class SecurityController {
    private static final String SECURITY = "security";
    private static final String PATH = "/" + SECURITY;
    private static final String MODEL_FORM = "form";
    private static final String MODEL_PASSWORD_FORM = "passwordForm";
    private static final String MODEL_PASSWORD_FORGOT_FORM = "forgotPasswordForm";
    private static final String MODEL_PASSWORD_RESET_FORM = "resetPasswordForm";
    private static final String MODEL_ALERT = "alert";
    private static final String MODEL_BREADCRUMBS = "breadcrumbs";

    // LOGIN
    private static final String ACTION_LOGIN = "login";
    private static final String VIEW_SECURITY_LOGIN = PATH + "/" + ACTION_LOGIN + ".html";
    private static final String PATH_LOGIN = PATH + "/" + ACTION_LOGIN;
    private static final URI URI_LOGIN = UriBuilder.of(PATH).path(ACTION_LOGIN).build();
    public static final @NonNull Message MESSAGE_LOG_IN = Message.of("Log in", "loginform.submit");
    public static final @NonNull InputSubmitFormElement INPUT_SUBMIT_LOGIN = InputSubmitFormElement.builder().value(MESSAGE_LOG_IN).build();

    private final Form loginForm;

    // SIGN UP
    private final Form signUpForm;
    private static final String VIEW_SECURITY_SIGN_UP = PATH + "/signUp.html";
    public static final String PATH_SIGN_UP = PATH + "/signUp";

    // PASSWORD
    private final Form forgotPasswordForm;
    private static final Message MESSAGE_FORGOT_SUBMIT = Message.of("Email me reset instructions", "resetpasswordform.submit");
    private static final Message MESSAGE_INSTRUCTIONS_SENT = Message.of("Check your email for reset instructions.", "password.forgot.sent");
    private static final Message MESSAGE_PASSWORD_RESET = Message.of("You have successfully reset your password.", "password.reset");
    private static final Message MESSAGE_TOKEN_INVALID = Message.of("Token is invalid or already expired.", ValidToken.class.getName() + ".message");
    private static final String ACTION_PASSWORD_CHANGE = "changePassword";
    private static final String ACTION_PASSWORD_UPDATE = "updatePassword";
    private static final String ACTION_PASSWORD_FORGOT = "forgotPassword";
    private static final String ACTION_PASSWORD_RESET = "resetPassword";
    private static final String PATH_PASSWORD_CHANGE = PATH + "/" + ACTION_PASSWORD_CHANGE;
    private static final String PATH_PASSWORD_UPDATE = PATH + "/" + ACTION_PASSWORD_UPDATE;
    private static final String PATH_PASSWORD_FORGOT = PATH + "/" + ACTION_PASSWORD_FORGOT;
    private static final String PATH_PASSWORD_RESET = PATH + "/" + ACTION_PASSWORD_RESET;
    private static final String VIEW_PASSWORD_CHANGE = PATH + "/" + ACTION_PASSWORD_CHANGE + ".html";
    private static final String VIEW_CHANGED_PASSWORD = PATH + "/passwordChanged.html";
    private static final String VIEW_PASSWORD_FORGOT = PATH + "/" + ACTION_PASSWORD_FORGOT + ".html";
    private static final String VIEW_PASSWORD_RESET = PATH + "/" + ACTION_PASSWORD_RESET + ".html";

    // BREADCRUMBS
    public static final Message MESSAGE_PASSWORD_CHANGE = Message.of("Change password", "profile.changePassword");
    public static final Breadcrumb BREADCRUMB_HOME = new Breadcrumb(Message.of("Home", "home"), "/");
    public static final Breadcrumb BREADCRUMB_PROFILE_SHOW = new Breadcrumb(Message.of("Profile", "profile.show"), "/profile/show");
    public static final Breadcrumb BREADCRUMB_PASSWORD_CHANGE_ACTIVE = new Breadcrumb(MESSAGE_PASSWORD_CHANGE);
    public static final Breadcrumb BREADCRUMB_PASSWORD_CHANGE = new Breadcrumb(MESSAGE_PASSWORD_CHANGE, PATH_PASSWORD_CHANGE);
    public static final Breadcrumb BREADCRUMB_PASSWORD_CHANGED = new Breadcrumb(Message.of("Password Changed", "nav.passwordChanged"));

    private final FormGenerator formGenerator;
    private final RegisterService registerService;
    private final PasswordService passwordService;
    private final HttpHostResolver httpHostResolver;
    private final HttpLocaleResolver httpLocaleResolver;

    SecurityController(FormGenerator formGenerator,
                       LoginControllerConfiguration loginControllerConfiguration,
                       RegisterService registerService,
                       PasswordService passwordService,
                       HttpHostResolver httpHostResolver,
                       HttpLocaleResolver httpLocaleResolver) {
        this.formGenerator = formGenerator;
        this.registerService = registerService;
        this.passwordService = passwordService;
        this.httpHostResolver = httpHostResolver;
        this.httpLocaleResolver = httpLocaleResolver;
        loginForm = formGenerator.generate(loginControllerConfiguration.getPath(), LoginForm.class, INPUT_SUBMIT_LOGIN);
        signUpForm = formGenerator.generate(PATH_SIGN_UP, SignUpForm.class);
        forgotPasswordForm = formGenerator.generate(PATH_PASSWORD_FORGOT, ForgotPasswordForm.class, MESSAGE_FORGOT_SUBMIT);
    }

    @GetHtml(uri = PATH_SIGN_UP, rolesAllowed = SecurityRule.IS_ANONYMOUS, view = VIEW_SECURITY_SIGN_UP)
    Map<String, Object> signUpCreate() {
        return Collections.singletonMap(MODEL_FORM, signUpForm);
    }

    @PostForm(uri = PATH_SIGN_UP, rolesAllowed = SecurityRule.IS_ANONYMOUS)
    HttpResponse<?> signUp(@NonNull @NotNull @Valid @Body SignUpForm form,
                           @Nullable Tenant tenant) {
        try {
            registerService.register(form.email(), form.password(), tenant);
        } catch (RegistrationCheckViolationException e) {
            return HttpResponse.unprocessableEntity().body(new ModelAndView<>(VIEW_SECURITY_SIGN_UP,
                    Map.of(
                            MODEL_FORM, signUpForm,
                            MODEL_ALERT, Alert.danger(e.getViolation().message()))));
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
        return Map.of(MODEL_PASSWORD_FORM, form,
                MODEL_BREADCRUMBS, List.of(BREADCRUMB_HOME, BREADCRUMB_PROFILE_SHOW, BREADCRUMB_PASSWORD_CHANGE_ACTIVE));
    }

    @PostForm(uri = PATH_PASSWORD_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> updatePassword(@NonNull @NotNull @Valid @Body PasswordForm form,
                                   @NonNull Authentication authentication) {
        final String userId = form.userId();
        if (userId.equals(authentication.getName())) {
            this.passwordService.updatePassword(userId, form.password());
            return HttpResponse.ok().body(new ModelAndView<>(VIEW_CHANGED_PASSWORD, Map.of(
                    MODEL_BREADCRUMBS, List.of(BREADCRUMB_HOME, BREADCRUMB_PROFILE_SHOW, BREADCRUMB_PASSWORD_CHANGE, BREADCRUMB_PASSWORD_CHANGED))));
        }
        return HttpResponse.seeOther(URI_LOGIN);
    }

    @GetHtml(uri = PATH_PASSWORD_FORGOT, rolesAllowed = SecurityRule.IS_ANONYMOUS, view = VIEW_PASSWORD_FORGOT)
    Map<String, Object> forgotPasswordForm() {
        return Map.of(MODEL_PASSWORD_FORGOT_FORM, forgotPasswordForm);
    }

    @PostForm(uri = PATH_PASSWORD_FORGOT, rolesAllowed = SecurityRule.IS_ANONYMOUS)
    HttpResponse<?> forgotPassword(@NonNull @NotNull HttpRequest<?> request,
                                   @NonNull @NotNull @Valid @Body ForgotPasswordForm forgotPasswordForm) {
        passwordService.sendResetInstructions(forgotPasswordForm.email(),
                httpLocaleResolver.resolveOrDefault(request),
                UriBuilder.of(httpHostResolver.resolve(request)).path(SecurityController.PATH_PASSWORD_RESET));
        return HttpResponse.ok().body(forgotPasswordModelAndView(forgotPasswordForm.email()));
    }

    @GetHtml(uri = PATH_PASSWORD_RESET, rolesAllowed = SecurityRule.IS_ANONYMOUS)
    ModelAndView<Map<String, Object>> resetPasswordForm(@ValidToken @QueryValue(PasswordService.TOKEN_QUERY_PARAM) String token) {
        return resetPasswordModelAndView(token);
    }

    @PostForm(uri = PATH_PASSWORD_RESET, rolesAllowed = SecurityRule.IS_ANONYMOUS)
    HttpResponse<?> resetPassword(@NonNull @NotNull @Valid @Body ResetPasswordForm resetPasswordForm) {
        final Map<String, ?> model = passwordService.resetPassword(resetPasswordForm.token(), resetPasswordForm.password())
                .map(email -> Map.of(MODEL_ALERT, Alert.info(MESSAGE_PASSWORD_RESET), MODEL_FORM,
                        formGenerator.generate(loginForm.action(), new LoginForm(email, null))))
                .orElseGet(() -> Map.of(MODEL_ALERT, Alert.danger(MESSAGE_TOKEN_INVALID), MODEL_FORM, loginForm));
        return HttpResponse.ok().body(new ModelAndView<>(VIEW_SECURITY_LOGIN, model));
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
            final Map<String, Object> model = Map.of(MODEL_PASSWORD_FORM, form,
                    MODEL_BREADCRUMBS, List.of(BREADCRUMB_HOME, BREADCRUMB_PROFILE_SHOW, BREADCRUMB_PASSWORD_CHANGE, BREADCRUMB_PASSWORD_CHANGED));
            return HttpResponse.ok().body(new ModelAndView<>(VIEW_PASSWORD_CHANGE, model));
        } else if (request.getPath().equals(PATH_PASSWORD_FORGOT)) {
            return request.getBody(ForgotPasswordForm.class)
                    .map(form -> HttpResponse.ok().body(new ModelAndView<>(VIEW_PASSWORD_FORGOT,
                            Map.of(MODEL_PASSWORD_FORGOT_FORM, (Object) formGenerator.generate(PATH_PASSWORD_FORGOT, form, ex, MESSAGE_FORGOT_SUBMIT)))))
                    .orElseGet(HttpResponse::serverError);
        } else if (request.getPath().equals(PATH_PASSWORD_RESET)) {
            return request.getBody(ResetPasswordForm.class)
                    .map(form -> HttpResponse.ok().body(new ModelAndView<>(VIEW_PASSWORD_RESET,
                            Map.of(MODEL_PASSWORD_RESET_FORM, (Object) formGenerator.generate(PATH_PASSWORD_RESET, form, ex)))))
                    .orElseGet(() -> HttpResponse.ok().body(new ModelAndView<>(VIEW_SECURITY_LOGIN,
                            Map.of(MODEL_FORM, loginForm, MODEL_ALERT, Alert.danger(MESSAGE_TOKEN_INVALID)))));
        }
        return HttpResponse.serverError();
    }

    @NonNull
    private ModelAndView<Map<String, Object>> resetPasswordModelAndView(@NonNull String token) {
        final ResetPasswordForm resetPasswordForm = new ResetPasswordForm(token);
        final Form form = formGenerator.generate(PATH_PASSWORD_RESET, resetPasswordForm);
        return new ModelAndView<>(VIEW_PASSWORD_RESET, Map.of(MODEL_PASSWORD_RESET_FORM, form));
    }

    @NonNull
    private ModelAndView<Map<String, Object>> forgotPasswordModelAndView(@NonNull String email) {
        final Form form = formGenerator.generate(loginForm.action(), new LoginForm(email, null));
        final Map<String, Object> model = Map.of(MODEL_FORM, form, MODEL_ALERT, Alert.info(MESSAGE_INSTRUCTIONS_SENT));
        return new ModelAndView<>(VIEW_SECURITY_LOGIN, model);
    }
}
