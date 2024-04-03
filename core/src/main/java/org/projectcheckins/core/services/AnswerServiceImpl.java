package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.markdown.MarkdownRenderer;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.ProfileRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Singleton
public class AnswerServiceImpl implements AnswerService {

    private static final PublicProfile UNKNOWN_RESPONDENT = new ProfileRecord(
            null,
            "unknown@respondent.com",
            TimeZone.getDefault(),
            DayOfWeek.MONDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
            Format.MARKDOWN,
            "Unknown",
            "Respondent"
    );

    private final AnswerRepository answerRepository;
    private final ProfileRepository profileRepository;
    private final MarkdownRenderer markdownRenderer;

    public AnswerServiceImpl(AnswerRepository answerRepository,
                             ProfileRepository profileRepository,
                             MarkdownRenderer markdownRenderer) {
        this.answerRepository = answerRepository;
        this.profileRepository = profileRepository;
        this.markdownRenderer = markdownRenderer;
    }

    @Override
    @NonNull
    public String save(@NotNull Authentication authentication,
                       @NotNull @Valid AnswerSave answerSave,
                       @Nullable Tenant tenant) {
        final AnswerRecord answer = new AnswerRecord(
                null,
                answerSave.questionId(),
                authentication.getName(),
                answerSave.answerDate(),
                answerSave.format(),
                answerSave.text()
        );
        return answerRepository.save(answer, tenant);
    }

    @Override
    @NonNull
    public List<AnswerViewRecord> findByQuestionId(@NotBlank String questionId,
                                                   @NotNull Authentication authentication,
                                                   @Nullable Tenant tenant) {
        final Map<String, PublicProfile> respondents = profileRepository.list(tenant).stream().collect(toMap(PublicProfile::id, Function.identity()));
        return answerRepository.findByQuestionId(questionId, tenant).stream()
                .map(answer -> buildView(answer, authentication.getName(), respondents))
                .toList();
    }

    private AnswerViewRecord buildView(Answer answer, String authenticatedUserId, Map<String, PublicProfile> respondents) {
        final String respondentId = answer.respondentId();
        final PublicProfile respondent = respondents.getOrDefault(respondentId, UNKNOWN_RESPONDENT);
        final String html = getHtml(answer);
        final boolean isEditable = respondentId.equals(authenticatedUserId);
        return new AnswerViewRecord(answer, respondent, html, isEditable);
    }

    private String getHtml(Answer answer) {
        if (answer.format() == Format.WYSIWYG) {
            return answer.text();
        }
        return markdownRenderer.render(answer.text());
    }
}
