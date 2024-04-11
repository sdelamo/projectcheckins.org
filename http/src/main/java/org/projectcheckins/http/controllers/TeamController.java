package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.multitenancy.Tenant;
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
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.core.services.TeamService;
import org.projectcheckins.security.UserAlreadyExistsException;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
class TeamController {

    private static final String TEAM = "team";
    public static final String PATH = ApiConstants.SLASH + TEAM;
    private static final String MODEL_MEMBERS = "members";
    private static final String MEMBER_FORM = "memberForm";

    // LIST
    public static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = PATH + ApiConstants.VIEW_LIST;
    public static final Breadcrumb BREADCRUMB_LIST = new Breadcrumb(Message.of("Team members", TEAM + ApiConstants.DOT + ApiConstants.ACTION_LIST), PATH_LIST);

    // CREATE
    private static final String PATH_CREATE = PATH + ApiConstants.PATH_CREATE;
    private static final String VIEW_CREATE = PATH + ApiConstants.VIEW_CREATE;
    private static final Breadcrumb BREADCRUMB_CREATE = new Breadcrumb(Message.of("Add team member", TEAM + ApiConstants.DOT + ApiConstants.ACTION_CREATE));

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;


    private final TeamService teamService;
    private final FormGenerator formGenerator;


    TeamController(TeamService teamService, FormGenerator formGenerator) {
        this.teamService = teamService;
        this.formGenerator = formGenerator;
    }

    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    Map<String, Object> memberList(@Nullable Tenant tenant) {
        return Map.of(
                ApiConstants.MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST),
                MODEL_MEMBERS, teamService.findAll(tenant)
        );
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> memberCreate() {
        return createModel();
    }

    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> memberSave(@NonNull @NotNull @Valid @Body TeamMemberSave form,
                               @Nullable Tenant tenant
    ) throws UserAlreadyExistsException {
        teamService.save(form, tenant);
        return HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request, ConstraintViolationException ex) {
        return request.getBody(TeamMemberSave.class)
                .map(form -> HttpResponse.unprocessableEntity().body(new ModelAndView<>(VIEW_CREATE, createModel(form, ex))))
                .orElseGet(HttpResponse::serverError);
    }

    private Map<String, Object> createModel() {
        return createModel(formGenerator.generate(PATH_SAVE, new TeamMemberSave(null)));
    }

    private Map<String, Object> createModel(TeamMemberSave teamMemberSave, ConstraintViolationException ex) {
        return createModel(formGenerator.generate(PATH_SAVE, teamMemberSave, ex));
    }

    private Map<String, Object> createModel(Form form) {
        return Map.of(
                ApiConstants.MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, BREADCRUMB_CREATE),
                MEMBER_FORM, form
        );
    }
}
