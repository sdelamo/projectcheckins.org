package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.messages.ConstraintViolationUtils;
import io.micronaut.views.fields.messages.Message;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.QuestionRepository;

import java.net.URI;
import java.time.DayOfWeek;
import java.util.*;
import java.util.function.Function;

@Controller
class QuestionController {

    private static final String QUESTION = "question";
    public static final String PATH = ApiConstants.SLASH + QUESTION;

    private static final String MODEL_QUESTIONS = "questions";
    private static final String MODEL_QUESTION = "question";

    // LIST
    public static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = PATH + ApiConstants.VIEW_LIST;
    private static final Breadcrumb BREADCRUMB_LIST = new Breadcrumb(Message.of("Questions", QUESTION + ApiConstants.DOT + ApiConstants.ACTION_LIST), PATH_LIST);

    // CREATE
    private static final String PATH_CREATE = PATH + ApiConstants.PATH_CREATE;
    private static final String VIEW_CREATE = PATH + ApiConstants.VIEW_CREATE;
    private static final Breadcrumb BREADCRUMB_CREATE = new Breadcrumb(Message.of("New Question", QUESTION + ApiConstants.DOT + ApiConstants.ACTION_CREATE));

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;

    // SHOW
    private static final String PATH_SHOW = PATH + ApiConstants.PATH_SHOW;
    public static final Function<String, URI> PATH_SHOW_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_SHOW).build();
    private static final String VIEW_SHOW = PATH + ApiConstants.VIEW_SHOW;

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;

    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final String REGEX_UPDATE = "^\\/question\\/.*\\/update$";
    private static final Function<String, URI> PATH_UPDATE_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_UPDATE).build();

    // DELETE
    private static final String PATH_DELETE = PATH + ApiConstants.PATH_DELETE;
    private static final String ANSWER_FORM = "answerForm";
    public static final String MODEL_FIELDSET = "fieldset";

    private final QuestionRepository questionRepository;

    private final AnswerSaveFormGenerator answerSaveFormGenerator;

    QuestionController(QuestionRepository questionRepository,
                       AnswerSaveFormGenerator answerSaveFormGenerator) {
        this.questionRepository = questionRepository;
        this.answerSaveFormGenerator = answerSaveFormGenerator;
    }

    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    Map<String, Object> questionList(@Nullable Tenant tenant) {
        return Map.of(MODEL_QUESTIONS, questionRepository.findAll(tenant));
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> questionCreate() {
        return Map.of(MODEL_FIELDSET, new QuestionSaveForm(null),
                ApiConstants.MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, BREADCRUMB_CREATE));
    }
    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionSave(@NonNull @NotNull @Valid @Body QuestionSaveForm form,
                                 @Nullable Tenant tenant) {
        String id = questionRepository.save(form, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> questionShow(@PathVariable @NotBlank String id,
                                 @NonNull Authentication authentication,
                                 @Nullable Tenant tenant) {
        Form answerFormSave = answerSaveFormGenerator.generate(id, format -> AnswerController.URI_BUILDER_ANSWER_SAVE.apply(id, format).toString(), authentication);
        return questionRepository.findById(id, tenant)
                .map(question -> HttpResponse.ok(Map.of(
                        MODEL_QUESTION, question,
                        ANSWER_FORM, answerFormSave
                )))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @Hidden
    @Produces(MediaType.TEXT_HTML)
    @Get(PATH_EDIT)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionEdit(@PathVariable @NotBlank String id,
                                 @Nullable Tenant tenant) {
        return questionRepository.findById(id, tenant)
                .map(question -> HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, updateModel(question))))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionUpdate(@PathVariable @NotBlank String id,
                                   @NonNull @NotNull @Valid @Body QuestionUpdateForm form,
                                   @Nullable Tenant tenant) {
        if (!id.equals(form.id())) {
            return HttpResponse.unprocessableEntity();
        }
        questionRepository.update(form, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }
 
    @PostForm(uri = PATH_DELETE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionDelete(@PathVariable @NotBlank String id,
                                   @Nullable Tenant tenant) {
        questionRepository.deleteById(id, tenant);
        return HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request,
                                                          @Nullable Tenant tenant,
                                                          ConstraintViolationException ex) {
        if (request.getPath().equals(PATH_SAVE)) {
            return request.getBody(QuestionSaveForm.class)
                    .map(form -> HttpResponse.unprocessableEntity()
                            .body(new ModelAndView<>(VIEW_CREATE,
                                    Collections.singletonMap(MODEL_FIELDSET,
                                            QuestionSaveForm.of(form, ex)))))
                    .orElseGet(HttpResponse::serverError);
        } else if (request.getPath().matches(REGEX_UPDATE)) {
            Optional<QuestionUpdateForm> updateFormOptional = request.getBody(QuestionUpdateForm.class);
            if (updateFormOptional.isEmpty()) {
                return HttpResponse.serverError();
            }
            QuestionUpdateForm form = updateFormOptional.get();
            return questionRepository.findById(form.id(), tenant)
                    .map(question -> HttpResponse.unprocessableEntity()
                            .body(new ModelAndView<>(VIEW_EDIT,
                                    Map.of(MODEL_QUESTION, question, MODEL_FIELDSET, QuestionUpdateForm.of(form, ex)))))
                    .orElseGet(NotFoundController::notFoundRedirect);
        }
        return HttpResponse.serverError();
    }

    @NonNull
    private Map<String, Object> updateModel(@NonNull Question question) {
        QuestionUpdateForm fieldset = new QuestionUpdateForm(question.id(),
                question.title(),
                question.howOften(),
                question.timeOfDay(),
                question.fixedTime(),
                question.howOften() == HowOften.DAILY_ON ? question.days() : Collections.singleton(DayOfWeek.MONDAY),
                question.howOften() == HowOften.ONCE_A_WEEK ? question.days().stream().findFirst().orElseThrow() : DayOfWeek.MONDAY,
                question.howOften() == HowOften.EVERY_OTHER_WEEK ? question.days().stream().findFirst().orElseThrow() : DayOfWeek.MONDAY,
                question.howOften() == HowOften.ONCE_A_MONTH_ON_THE_FIRST ? question.days().stream().findFirst().orElseThrow() : DayOfWeek.MONDAY
        );
        return Map.of(MODEL_QUESTION, question, MODEL_FIELDSET, fieldset);
    }

}
