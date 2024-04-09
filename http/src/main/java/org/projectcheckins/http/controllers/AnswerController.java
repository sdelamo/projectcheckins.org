package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
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
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.AnswerView;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.AnswerForm;
import org.projectcheckins.core.forms.AnswerMarkdownSave;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerWysiwygSave;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.services.AnswerService;
import org.projectcheckins.core.services.QuestionService;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Controller
class AnswerController {
    private static final String ANSWER = "answer";
    private static final String MODEL_ANSWER = "answer";
    private static final String PATH = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER;

    // Answer save WYSIWYG
    private static final String MARKDOWN = "markdown";

    // Answer save markdown
    private static final String WYSIWYG = "wysiwyg";

    // SAVE
    private static final String PATH_ANSWER_SAVE_MARKDOWN = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER + ApiConstants.SLASH + MARKDOWN;
    private static final String REGEX_SAVE_MARKDOWN = "^\\/question\\/.*\\/answer/markdown$";
    private static final Function<String, URI> URI_BUILDER_ANSWER_SAVE_MARKDOWN = questionId ->
            UriBuilder.of(QuestionController.PATH).path(questionId).path(ANSWER).path(MARKDOWN).build();

    private static final String PATH_ANSWER_SAVE_WYSIWYG = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER + ApiConstants.SLASH + WYSIWYG;
    private static final String REGEX_SAVE_WYSIWYG = "^\\/question\\/.*\\/answer/wysiwyg$";
    private static final Function<String, URI> URI_BUILDER_ANSWER_SAVE_WYSIWYG = questionId ->
            UriBuilder.of(QuestionController.PATH).path(questionId).path(ANSWER).path(WYSIWYG).build();

    public static final BiFunction<String, Format, URI> URI_BUILDER_ANSWER_SAVE = (questionId, format) -> switch (format) {
        case MARKDOWN -> URI_BUILDER_ANSWER_SAVE_MARKDOWN.apply(questionId);
        case WYSIWYG -> URI_BUILDER_ANSWER_SAVE_WYSIWYG.apply(questionId);
    };

    // SHOW
    private static final String PATH_SHOW = PATH + ApiConstants.PATH_SHOW;
    public static final Function<Answer, URI> PATH_SHOW_BUILDER  = answer -> UriBuilder.of(QuestionController.PATH).path(answer.questionId()).path(ANSWER).path(answer.id()).path(ApiConstants.ACTION_SHOW).build();
    private static final String VIEW_SHOW = ANSWER + ApiConstants.VIEW_SHOW;


    private final QuestionService questionService;
    private final AnswerService answerService;
    private final FormGenerator formGenerator;

    AnswerController(QuestionService questionService, AnswerService answerService, FormGenerator formGenerator) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.formGenerator = formGenerator;
    }

    @PostForm(uri = PATH_ANSWER_SAVE_MARKDOWN, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerSaveMarkdown(@PathVariable String questionId,
                                       @NonNull Authentication authentication,
                                       @Nullable Tenant tenant,
                                       @Body @NonNull @NotNull @Valid AnswerMarkdownSave form) {
        if (!questionId.equals(form.questionId())) {
            return HttpResponse.unprocessableEntity();
        }
        if (!authentication.getName().equals(form.respondentId())) {
            return HttpResponse.unprocessableEntity();
        }
        answerService.save(authentication, new AnswerSave(form.questionId(), form.answerDate(), Format.MARKDOWN, form.markdown()), tenant);
        return HttpResponse.seeOther(QuestionController.PATH_SHOW_BUILDER.apply(questionId));
    }

    @PostForm(uri = PATH_ANSWER_SAVE_WYSIWYG, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerSaveWysiwyg(@PathVariable String questionId,
                                      @NonNull Authentication authentication,
                                      @Nullable Tenant tenant,
                                      @Body @NonNull @NotNull @Valid AnswerWysiwygSave form) {
        if (!questionId.equals(form.questionId())) {
            return HttpResponse.unprocessableEntity();
        }
        if (!authentication.getName().equals(form.respondentId())) {
            return HttpResponse.unprocessableEntity();
        }
        answerService.save(authentication, new AnswerSave(form.questionId(), form.answerDate(), Format.WYSIWYG, form.html()), tenant);
        return HttpResponse.seeOther(QuestionController.PATH_SHOW_BUILDER.apply(questionId));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> answerShow(@PathVariable @NotBlank String questionId,
                               @PathVariable @NotBlank String id,
                               @NonNull Authentication authentication,
                               @Nullable Locale locale,
                               @Nullable Tenant tenant) {
        return questionService.findById(questionId, tenant)
                .flatMap(question -> answerService.findById(id, authentication, tenant)
                            .map(answer -> HttpResponse.ok(Map.of(
                                    MODEL_ANSWER, answer,
                                    ApiConstants.MODEL_BREADCRUMBS, List.of(
                                            QuestionController.BREADCRUMB_LIST,
                                            QuestionController.BREADCRUMB_SHOW.apply(question),
                                            makeBreadcrumbShow(answer, locale))
                                    ))))
                .orElseGet(NotFoundController::notFoundRedirect);
    }


    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request,
                                                          @NotNull Authentication authentication,
                                                          @Nullable Tenant tenant,
                                                          ConstraintViolationException ex) {
        final Optional<? extends AnswerForm> answerForm;
        if (request.getPath().matches(REGEX_SAVE_MARKDOWN)) {
            answerForm = request.getBody(AnswerMarkdownSave.class);
        } else if (request.getPath().matches(REGEX_SAVE_WYSIWYG)) {
            answerForm = request.getBody(AnswerWysiwygSave.class);
        } else {
            answerForm = Optional.empty();
        }
        return answerForm.map(f -> questionService.findById(f.questionId(), tenant)
                        .map(q -> HttpResponse.unprocessableEntity().body(new ModelAndView<>(QuestionController.VIEW_SHOW, showQuestionModel(q, generateForm(f, ex), authentication, tenant))))
                        .orElseGet(NotFoundController::notFoundRedirect))
                .orElseGet(HttpResponse::serverError);
    }

    private Breadcrumb makeBreadcrumbShow(AnswerView view, Locale locale) {
        return new Breadcrumb(Message.of(answerService.getAnswerSummary(view, locale)), PATH_SHOW_BUILDER.andThen(URI::toString).apply(view.answer()));
    }

    @NonNull
    private Map<String, Object> showQuestionModel(@NonNull Question question, @NonNull Form answerFormSave, @NonNull Authentication authentication, Tenant tenant) {
        return Map.of(
                QuestionController.MODEL_QUESTION, question,
                ApiConstants.MODEL_BREADCRUMBS, List.of(QuestionController.BREADCRUMB_LIST, new Breadcrumb(Message.of(question.title()))),
                QuestionController.MODEL_ANSWERS, answerService.findByQuestionId(question.id(), authentication, tenant),
                QuestionController.ANSWER_FORM, answerFormSave
        );
    }

    private Form generateForm(AnswerForm form, ConstraintViolationException ex) {
        return formGenerator.generate(URI_BUILDER_ANSWER_SAVE.apply(form.questionId(), getFormat(form)).toString(), form, ex);
    }

    private static Format getFormat(AnswerForm form) {
        return switch (form) {
            case AnswerMarkdownSave markdown -> Format.MARKDOWN;
            case AnswerWysiwygSave wysiwyg -> Format.WYSIWYG;
            default -> null;
        };
    }
}
