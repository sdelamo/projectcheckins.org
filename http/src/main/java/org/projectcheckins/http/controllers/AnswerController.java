package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.core.forms.AnswerMarkdownSave;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerWysiwygSave;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;

@Controller
class AnswerController {
    private static final String ANSWER = "answer";
    private static final String PATH = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER;

    // Answer save WYSIWYG
    private static final String MARKDOWN = "markdown";

    // Answer save markdown
    private static final String WYSIWYG = "wysiwyg";

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

    private final AnswerRepository answerRepository;

    AnswerController(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @PostForm(uri = PATH_ANSWER_SAVE_MARKDOWN, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerSaveMarkdown(@PathVariable String questionId,
                                       @NonNull Authentication authentication,
                                       @Body @NonNull @NotNull @Valid AnswerMarkdownSave form) {
        if (!questionId.equals(form.questionId())) {
            return HttpResponse.unprocessableEntity();
        }
        answerRepository.save(authentication, new AnswerSave(form.questionId(), form.answerDate(), Format.MARKDOWN, form.markdown()));
        return HttpResponse.seeOther(QuestionController.PATH_SHOW_BUILDER.apply(questionId));
    }

    @PostForm(uri = PATH_ANSWER_SAVE_WYSIWYG, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerSaveWysiwyg(@PathVariable String questionId,
                                      @NonNull Authentication authentication,
                                      @Body @NonNull @NotNull @Valid AnswerWysiwygSave form) {
        if (!questionId.equals(form.questionId())) {
            return HttpResponse.unprocessableEntity();
        }
        answerRepository.save(authentication, new AnswerSave(form.questionId(), form.answerDate(), Format.WYSIWYG, form.html()));
        return HttpResponse.seeOther(QuestionController.PATH_SHOW_BUILDER.apply(questionId));
    }
}
