package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.repositories.ProfileRepository;

@Controller
class ProfileController {

    private static final String PROFILE = "profile";
    private static final String PATH = ApiConstants.SLASH + PROFILE;

    private static final String MODEL_PROFILE = "profile";

    // SHOW
    private static final String PATH_SHOW = PATH + ApiConstants.SLASH + ApiConstants.ACTION_SHOW;
    private static final String VIEW_SHOW = PATH + ApiConstants.VIEW_SHOW;

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.SLASH + ApiConstants.ACTION_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;

    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.SLASH + ApiConstants.ACTION_UPDATE;

    private final FormGenerator formGenerator;
    private final ProfileRepository profileRepository;

    ProfileController(FormGenerator formGenerator, ProfileRepository profileRepository) {
        this.formGenerator = formGenerator;
        this.profileRepository = profileRepository;
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> profileShow(@NonNull @NotNull Authentication authentication, @Nullable Tenant tenant) {
        return profileRepository.findByAuthentication(authentication, tenant)
            .map(p -> HttpResponse.ok(Map.of(MODEL_PROFILE, p)))
            .orElseGet(NotFoundController::notFoundRedirect);
    }

    @GetHtml(uri = PATH_EDIT, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_EDIT)
    HttpResponse<?> profileEdit(@NonNull @NotNull Authentication authentication, @Nullable Tenant tenant) {
        return profileRepository.findByAuthentication(authentication, tenant)
            .map(p -> HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, updateModel(p))))
            .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> profileUpdate(@NonNull @NotNull Authentication authentication,
                                  @NonNull @NotNull @Valid @Body ProfileUpdate profileUpdate,
                                  @Nullable Tenant tenant) {
        profileRepository.update(authentication, profileUpdate, tenant);
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
        return Map.of(ApiConstants.MODEL_FORM, form);
    }
}
