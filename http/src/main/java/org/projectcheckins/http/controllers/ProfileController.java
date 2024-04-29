package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.messages.Message;
import io.micronaut.views.turbo.http.TurboMediaType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.repositories.ProfileRepository;

import org.projectcheckins.security.http.TurboFrameUtils;
import org.projectcheckins.security.http.TurboStreamUtils;
import static org.projectcheckins.http.controllers.ApiConstants.*;

@Controller
class ProfileController {

    private static final String PROFILE = "profile";
    private static final String PATH = SLASH + PROFILE;

    private static final String MODEL_PROFILE = "profile";

    // SHOW
    public static final String VIEW_SHOW_FRAGMENT = MODEL_PROFILE + SLASH + ApiConstants.FRAGMENT_SHOW;

    private static final Message MESSAGE_SHOW = Message.of("Profile", PROFILE + ApiConstants.DOT + ApiConstants.ACTION_SHOW);
    private static final String PATH_SHOW = PATH + SLASH + ApiConstants.ACTION_SHOW;
    private static final String VIEW_SHOW = PATH + ApiConstants.VIEW_SHOW;

    // EDIT
    private static final String VIEW_EDIT_FRAGMENT = PATH + SLASH + FRAGMENT_EDIT;

    private static final Message MESSAGE_BREADCRUMB_EDIT = Message.of("Edit", PROFILE + ApiConstants.DOT + ApiConstants.ACTION_EDIT);
    private static final Breadcrumb BREADCRUMB_EDIT = new Breadcrumb(MESSAGE_BREADCRUMB_EDIT);
    private static final String PATH_EDIT = PATH + SLASH + ApiConstants.ACTION_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;

    // UPDATE
    private static final String PATH_UPDATE = PATH + SLASH + ApiConstants.ACTION_UPDATE;

    private final FormGenerator formGenerator;
    private final ProfileRepository profileRepository;

    ProfileController(FormGenerator formGenerator, ProfileRepository profileRepository) {
        this.formGenerator = formGenerator;
        this.profileRepository = profileRepository;
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> profileShow(@NonNull @NotNull HttpRequest<?> request,
                                @NonNull @NotNull Authentication authentication,
                                @Nullable Tenant tenant) {
        return showModel(authentication, tenant)
                .map(model -> TurboFrameUtils.turboFrame(request)
                        .map(frame -> (Object) TurboFrameUtils.turboFrame(frame, VIEW_SHOW_FRAGMENT, model))
                        .orElseGet(() -> new ModelAndView<>(VIEW_SHOW, model)))
                .map(HttpResponse::ok)
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @GetHtml(uri = PATH_EDIT, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> profileEdit(@NonNull @NotNull HttpRequest<?> request,
                                @NonNull @NotNull Authentication authentication,
                                @Nullable Tenant tenant) {
        return profileRepository.findByAuthentication(authentication, tenant)
                .map(this::updateModel)
                .map(model -> TurboFrameUtils.turboFrame(request)
                        .map(frame -> (Object) TurboFrameUtils.turboFrame(frame, VIEW_EDIT_FRAGMENT, model))
                        .orElseGet(() -> new ModelAndView<>(VIEW_EDIT, model)))
                .map(HttpResponse::ok)
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> profileUpdate(
            @NonNull @NotNull HttpRequest<?> request,
            @NonNull @NotNull Authentication authentication,
            @NonNull @NotNull @Valid @Body ProfileUpdate profileUpdate,
            @Nullable Tenant tenant) {
        profileRepository.update(authentication, profileUpdate, tenant);
        if (TurboMediaType.acceptsTurboStream(request)) {
            return showModel(authentication, tenant)
                    .flatMap(model -> TurboStreamUtils.turboStream(request, VIEW_SHOW_FRAGMENT, model))
                    .map(HttpResponse::ok)
                    .orElseGet(HttpResponse::notFound);
        }
        return HttpResponse.seeOther(URI.create(PATH_SHOW));
    }

    private Map<String, Object> updateModel(@NonNull Profile profile) {
        Form form = formGenerator.generate(PATH_UPDATE, new ProfileUpdate(
                profile.timeZone(),
                profile.firstDayOfWeek(),
                profile.beginningOfDay(),
                profile.endOfDay(),
                profile.timeFormat(),
                profile.format(),
                profile.firstName(),
                profile.lastName()));
        return Map.of(
                ApiConstants.MODEL_FORM, form,
                ApiConstants.MODEL_BREADCRUMBS, List.of(HomeController.BREADCRUMB_HOME, new Breadcrumb(MESSAGE_SHOW, PATH_SHOW), BREADCRUMB_EDIT)
        );
    }

    private Optional<Map<String, Object>> showModel(@NonNull Authentication authentication,
                                                    @Nullable Tenant tenant) {
        return profileRepository.findByAuthentication(authentication, tenant)
                .map(p -> Map.of(
                        MODEL_PROFILE, p,
                        ApiConstants.MODEL_BREADCRUMBS, List.of(HomeController.BREADCRUMB_HOME, new Breadcrumb(MESSAGE_SHOW)))
                );
    }
}
