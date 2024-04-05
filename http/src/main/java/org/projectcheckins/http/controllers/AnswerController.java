package org.projectcheckins.http.controllers;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.AnswerView;
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
    private static final Function<String, URI> URI_BUILDER_ANSWER_SAVE_MARKDOWN = questionId ->
            UriBuilder.of(QuestionController.PATH).path(questionId).path(ANSWER).path(MARKDOWN).build();

    private static final String PATH_ANSWER_SAVE_WYSIWYG = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER + ApiConstants.SLASH + WYSIWYG;
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
    private final MessageSource messageSource;

    AnswerController(QuestionService questionService, AnswerService answerService, MessageSource messageSource) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.messageSource = messageSource;
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

    private Breadcrumb makeBreadcrumbShow(AnswerView view, Locale locale) {
        return new Breadcrumb(Message.of(answerService.getAnswerSummary(view, locale)), PATH_SHOW_BUILDER.andThen(URI::toString).apply(view.answer()));
    }
}
