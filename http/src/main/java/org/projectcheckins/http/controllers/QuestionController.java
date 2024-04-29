package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.messages.Message;
import io.micronaut.views.turbo.TurboStream;
import io.micronaut.views.turbo.http.TurboHttpHeaders;
import io.micronaut.views.turbo.http.TurboMediaType;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.services.AnswerService;
import org.projectcheckins.core.services.QuestionService;
import org.projectcheckins.security.http.TurboFrameUtils;
import org.projectcheckins.security.http.TurboStreamUtils;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.projectcheckins.http.controllers.ApiConstants.*;
import static org.projectcheckins.http.controllers.ApiConstants.FRAME_ID_MAIN;
import static org.projectcheckins.http.controllers.ApiConstants.SLASH;

@Controller
class QuestionController {

    private static final String QUESTION = "question";
    public static final String PATH = SLASH + QUESTION;

    private static final String MODEL_QUESTIONS = "questions";
    public static final String MODEL_QUESTION = "question";
    private static final String MODEL_RESPONDENTS = "respondents";
    public static final String MODEL_ANSWERS = "answers";

    // LIST
    private static final String VIEW_LIST_FRAGMENT = PATH + SLASH + FRAGMENT_LIST;
    public static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = PATH + ApiConstants.VIEW_LIST;
    public static final Message MESSAGE_QUESTIONS = Message.of("Questions", QUESTION + DOT + ACTION_LIST);
    public static final Breadcrumb BREADCRUMB_LIST = new Breadcrumb(MESSAGE_QUESTIONS, PATH_LIST);

    // CREATE
    private static final String PATH_CREATE = PATH + ApiConstants.PATH_CREATE;
    private static final String VIEW_CREATE = PATH + ApiConstants.VIEW_CREATE;
    private static final String VIEW_CREATE_FRAGMENT = PATH + SLASH + FRAGMENT_CREATE;
    private static final Breadcrumb BREADCRUMB_CREATE = new Breadcrumb(Message.of("New Question", QUESTION + DOT + ACTION_CREATE));

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;

    // SHOW
    private static final String PATH_SHOW = PATH + ApiConstants.PATH_SHOW;
    public static final Function<String, URI> PATH_SHOW_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ACTION_SHOW).build();
    public static final String VIEW_SHOW = PATH + ApiConstants.VIEW_SHOW;
    public static final String VIEW_SHOW_FRAGMENT =  PATH + SLASH + FRAGMENT_SHOW;
    public static final Function<Question, Breadcrumb> BREADCRUMB_SHOW = question -> new Breadcrumb(Message.of(question.title()), PATH_SHOW_BUILDER.andThen(URI::toString).apply(question.id()));

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;
    private static final String VIEW_EDIT_FRAGMENT = PATH + SLASH + FRAGMENT_EDIT;
    private static final Breadcrumb BREADCRUMB_EDIT = new Breadcrumb(Message.of("Edit Question", QUESTION + DOT + ACTION_EDIT));

    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final Pattern REGEX_UPDATE = Pattern.compile("^\\/question\\/(.*)\\/update$");
    private static final Function<String, URI> PATH_UPDATE_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ACTION_UPDATE).build();

    // DELETE
    private static final String PATH_DELETE = PATH + ApiConstants.PATH_DELETE;
    public static final String ANSWER_FORM = "answerForm";
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

    @GetHtml(uri = PATH_LIST,
            rolesAllowed = SecurityRule.IS_AUTHENTICATED,
            view = VIEW_LIST,
            turboView = VIEW_LIST_FRAGMENT)
    Map<String, Object> questionList(@Nullable Tenant tenant) {
        return listModel(tenant);
    }

    @GetHtml(uri = PATH_CREATE,
            rolesAllowed = SecurityRule.IS_AUTHENTICATED,
            view = VIEW_CREATE,
            turboView = VIEW_CREATE_FRAGMENT)
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
    HttpResponse<?> questionSave(@NonNull @NotNull HttpRequest<?> request,
                                 @NonNull @NotNull @Valid @Body QuestionFormRecord form,
                                 @NonNull Authentication authentication,
                                 @Nullable Tenant tenant) {
        String id = questionService.save(form, tenant);
        return TurboMediaType.acceptsTurboStream(request)
                ? showTurboStream(request, id, authentication, tenant).map(HttpResponse::ok).orElseGet(HttpResponse::notFound)
                : HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionShow(HttpRequest<?> request,
                                 @PathVariable @NotBlank String id,
                                 @NonNull Authentication authentication,
                                 @Nullable Tenant tenant) {
        return showModel(answerService, questionService, answerSaveFormGenerator, id, authentication, tenant)
                .map(model -> TurboFrameUtils.turboFrame(request, VIEW_SHOW_FRAGMENT, model)
                        .orElseGet(() -> new ModelAndView<>(VIEW_SHOW, model)))
                .map(HttpResponse::ok)
                .orElseGet(NotFoundController::notFoundRedirect);
    }


    @GetHtml(uri = PATH_EDIT, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionEdit(HttpRequest<?> request,
                                 @PathVariable @NotBlank String id,
                                 @Nullable Tenant tenant) {
        return questionService.findById(id, tenant)
                .map(question -> updateModel(question, QuestionFormRecord.of(question), tenant))
                .map(model -> TurboFrameUtils.turboFrame(request)
                        .map(frame -> (Object) TurboFrameUtils.turboFrame(frame, VIEW_EDIT_FRAGMENT, model))
                        .orElseGet(() -> new ModelAndView<>(VIEW_EDIT, model)))
                .map(HttpResponse::ok)
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)

    HttpResponse<?> questionUpdate(@NonNull @NotNull HttpRequest<?> request,
                                   @NonNull Authentication authentication,
                                   @PathVariable @NotBlank String id,
                                   @NonNull @NotNull @Valid @Body QuestionFormRecord form,
                                   @Nullable Tenant tenant) {
        questionService.update(id, form, tenant);
        if (TurboMediaType.acceptsTurboStream(request)) {
            return showTurboStream(request, id, authentication, tenant)
                    .map(HttpResponse::ok)
                    .orElseGet(HttpResponse::notFound);
        }
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @PostForm(uri = PATH_DELETE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionDelete(HttpRequest<?> request,
                                   @PathVariable @NotBlank String id,
                                   @Nullable Tenant tenant) {
        questionService.deleteById(id, tenant);
        return TurboMediaType.acceptsTurboStream(request)
                ? HttpResponse.ok(TurboStreamUtils.turboStream(request, VIEW_LIST_FRAGMENT, listModel(tenant)))
                : HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request,
                                                          @Nullable Tenant tenant,
                                                          ConstraintViolationException ex) {
        String turboFrame = request.getHeaders().get(TurboHttpHeaders.TURBO_FRAME, String.class, FRAME_ID_MAIN);
        boolean turboRequest = TurboMediaType.acceptsTurboStream(request);
        String contentType = turboRequest ? TurboMediaType.TURBO_STREAM : MediaType.TEXT_HTML;

        final Matcher matcher = REGEX_UPDATE.matcher(request.getPath());
        if (request.getPath().equals(PATH_SAVE)) {
            return request.getBody(QuestionForm.class)
                    .map(form -> saveModel(QuestionFormRecord.of(form, ex), tenant))
                    .map(model -> unprocessableEntity(model, contentType, turboRequest, VIEW_CREATE, VIEW_CREATE_FRAGMENT, turboFrame))
                    .orElseGet(HttpResponse::serverError);
        } else if (matcher.find()) {
            final String id = matcher.group(1);
            Optional<QuestionFormRecord> updateFormOptional = request.getBody(QuestionFormRecord.class);
            if (updateFormOptional.isEmpty()) {
                return HttpResponse.serverError();
            }
            QuestionForm form = updateFormOptional.get();
            return questionService.findById(id, tenant)
                    .map(question -> updateModel(question, QuestionFormRecord.of(form, ex), tenant))
                    .map(model -> unprocessableEntity(model, contentType, turboRequest, VIEW_EDIT, VIEW_EDIT_FRAGMENT, turboFrame))
                    .orElseGet(NotFoundController::notFoundRedirect);
        }
        return HttpResponse.serverError();
    }

    @NonNull
    private HttpResponse<?> unprocessableEntity(Object model,
                                                String contentType,
                                                boolean turboRequest,
                                                String view,
                                                String turboView,
                                                @Nullable String turboFrame) {
        return HttpResponse.unprocessableEntity().body(turboRequest
                        ? TurboStream.builder().targetDomId(turboFrame).template(turboView, model).update()
                        : new ModelAndView<>(view, model));
    }

    @NonNull
    private Map<String, Object> updateModel(@NonNull Question question, @NonNull QuestionForm fieldset, @Nullable Tenant tenant) {
        return Map.of(
                MODEL_QUESTION, question,
                MODEL_FIELDSET, fieldset,
                MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, BREADCRUMB_SHOW.apply(question), BREADCRUMB_EDIT),
                MODEL_RESPONDENTS, questionService.listAvailableRespondents(tenant)
        );
    }

    @NonNull
    private Map<String, Object> saveModel(@NonNull QuestionForm fieldset, Tenant tenant) {
        return Map.of(
                MODEL_FIELDSET, fieldset,
                MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, BREADCRUMB_CREATE),
                MODEL_RESPONDENTS, questionService.listAvailableRespondents(tenant)
        );
    }

    @NonNull
    public static Map<String, Object> showModel(AnswerService answerService,
                                                Question question,
                                                Form answerFormSave,
                                                Authentication authentication,
                                                Tenant tenant) {
        return Map.of(
                MODEL_QUESTION, question,
                MODEL_BREADCRUMBS, List.of(BREADCRUMB_LIST, new Breadcrumb(Message.of(question.title()))),
                MODEL_ANSWERS, answerService.findByQuestionIdGroupedByDate(question.id(), authentication, tenant),
                ANSWER_FORM, answerFormSave
        );
    }

    @NonNull
    public static Optional<Map<String, Object>> showModel(@NonNull AnswerService answerService,
                                                          @NonNull QuestionService questionService,
                                                          @NonNull AnswerSaveFormGenerator answerSaveFormGenerator,
                                                          @NonNull String id,
                                                          @NonNull Authentication authentication,
                                                          @NonNull Tenant tenant) {
        Form answerFormSave = answerSaveFormGenerator.generate(id, format -> AnswerController.URI_BUILDER_ANSWER_SAVE.apply(id, format).toString(), authentication);
        return questionService.findById(id, tenant)
                .map(question -> showModel(answerService, question, answerFormSave, authentication, tenant));
    }

    private Optional<TurboStream.Builder> showTurboStream(HttpRequest<?> request,
                                                          @NonNull String questionid,
                                                          @NonNull Authentication authentication,
                                                          @Nullable Tenant tenant) {
        return showModel(answerService, questionService, answerSaveFormGenerator, questionid, authentication, tenant)
                .flatMap(model -> TurboStreamUtils.turboStream(request, VIEW_SHOW_FRAGMENT, model));
    }

    @NonNull
    private Map<String, Object> listModel(@Nullable Tenant tenant) {
        return Map.of(MODEL_QUESTIONS, questionService.findAll(tenant),
                MODEL_BREADCRUMBS, List.of(new Breadcrumb(MESSAGE_QUESTIONS)));
    }
}
