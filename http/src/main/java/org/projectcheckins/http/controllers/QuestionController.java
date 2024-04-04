package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
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
import org.projectcheckins.core.api.Respondent;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.services.AnswerService;
import org.projectcheckins.core.services.QuestionService;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
class QuestionController {

    private static final String QUESTION = "question";
    public static final String PATH = ApiConstants.SLASH + QUESTION;

    private static final String MODEL_QUESTIONS = "questions";
    private static final String MODEL_QUESTION = "question";
    private static final String MODEL_RESPONDENTS = "respondents";
    private static final String MODEL_ANSWERS = "answers";

    // LIST
    public static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = PATH + ApiConstants.VIEW_LIST;
    public static final Breadcrumb BREADCRUMB_LIST = new Breadcrumb(Message.of("Questions", QUESTION + ApiConstants.DOT + ApiConstants.ACTION_LIST), PATH_LIST);

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
    public static final Function<Question, Breadcrumb> BREADCRUMB_SHOW = question -> new Breadcrumb(Message.of(question.title()), PATH_SHOW_BUILDER.andThen(URI::toString).apply(question.id()));

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;
    private static final Breadcrumb BREADCRUMB_EDIT = new Breadcrumb(Message.of("Edit Question", QUESTION + ApiConstants.DOT + ApiConstants.ACTION_EDIT));

    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final Pattern REGEX_UPDATE = Pattern.compile("^\\/question\\/(.*)\\/update$");
    private static final Function<String, URI> PATH_UPDATE_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_UPDATE).build();

    // DELETE
    private static final String PATH_DELETE = PATH + ApiConstants.PATH_DELETE;
    private static final String ANSWER_FORM = "answerForm";
    public static final String MODEL_FIELDSET = "fieldset";

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final AnswerSaveFormGenerator answerSaveFormGenerator;

    QuestionController(QuestionService questionService,
                       AnswerService answerService,
                       AnswerSaveFormGenerator answerSaveFormGenerator) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.answerSaveFormGenerator = answerSaveFormGenerator;
    }

    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    Map<String, Object> questionList(@Nullable Tenant tenant) {
        return Map.of(MODEL_QUESTIONS, questionService.findAll(tenant));
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> questionCreate(@Nullable Tenant tenant) {
        final QuestionForm form = QuestionFormRecord.of(new QuestionRecord(
                null,
                "",
                HowOften.DAILY_ON,
                Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                TimeOfDay.END,
                LocalTime.of(16, 30),
                Collections.emptySet()
        ));
        return saveModel(form, tenant);
    }
    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionSave(@NonNull @NotNull @Valid @Body QuestionFormRecord form,
                                 @Nullable Tenant tenant) {
        String id = questionService.save(form, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> questionShow(@PathVariable @NotBlank String id,
                                 @NonNull Authentication authentication,
                                 @Nullable Tenant tenant) {
        Form answerFormSave = answerSaveFormGenerator.generate(id, format -> AnswerController.URI_BUILDER_ANSWER_SAVE.apply(id, format).toString(), authentication);
        return questionService.findById(id, tenant)
                .map(question -> HttpResponse.ok(Map.of(
                        MODEL_QUESTION, question,
                        ApiConstants.MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, new Breadcrumb(Message.of(question.title()))),
                        MODEL_ANSWERS, answerService.findByQuestionId(id, authentication, tenant),
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
        return questionService.findById(id, tenant)
                .map(question -> HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, updateModel(question, QuestionFormRecord.of(question), tenant))))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionUpdate(@PathVariable @NotBlank String id,
                                   @NonNull @NotNull @Valid @Body QuestionFormRecord form,
                                   @Nullable Tenant tenant) {
        questionService.update(id, form, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @PostForm(uri = PATH_DELETE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionDelete(@PathVariable @NotBlank String id,
                                   @Nullable Tenant tenant) {
        questionService.deleteById(id, tenant);
        return HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request,
                                                          @Nullable Tenant tenant,
                                                          ConstraintViolationException ex) {
        final Matcher matcher = REGEX_UPDATE.matcher(request.getPath());
        if (request.getPath().equals(PATH_SAVE)) {
            return request.getBody(QuestionForm.class)
                    .map(form -> HttpResponse.unprocessableEntity()
                            .body(new ModelAndView<>(VIEW_CREATE, saveModel(QuestionFormRecord.of(form, ex), tenant))))
                    .orElseGet(HttpResponse::serverError);
        } else if (matcher.find()) {
            final String id = matcher.group(1);
            Optional<QuestionFormRecord> updateFormOptional = request.getBody(QuestionFormRecord.class);
            if (updateFormOptional.isEmpty()) {
                return HttpResponse.serverError();
            }
            QuestionForm form = updateFormOptional.get();
            return questionService.findById(id, tenant)
                    .map(question -> HttpResponse.unprocessableEntity()
                            .body(new ModelAndView<>(VIEW_EDIT, updateModel(question, QuestionFormRecord.of(form, ex), tenant))))
                    .orElseGet(NotFoundController::notFoundRedirect);
        }
        return HttpResponse.serverError();
    }

    @NonNull
    private Map<String, Object> updateModel(@NonNull Question question, @NonNull QuestionForm fieldset, @Nullable Tenant tenant) {
        return Map.of(
                MODEL_QUESTION, question,
                MODEL_FIELDSET, fieldset,
                ApiConstants.MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, BREADCRUMB_SHOW.apply(question), BREADCRUMB_EDIT),
                MODEL_RESPONDENTS, questionService.listAvailableRespondents(tenant)
        );
    }

    @NonNull
    private Map<String, Object> saveModel(@NonNull QuestionForm fieldset, Tenant tenant) {
        return Map.of(
                MODEL_FIELDSET, fieldset,
                ApiConstants.MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, BREADCRUMB_CREATE),
                MODEL_RESPONDENTS, questionService.listAvailableRespondents(tenant)
        );
    }

}
