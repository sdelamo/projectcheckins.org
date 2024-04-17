package org.projectcheckins.core.services;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.AnswerView;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.markdown.MarkdownRenderer;
import org.projectcheckins.core.models.DateAnswers;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.ProfileRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import static java.time.format.DateTimeFormatterBuilder.getLocalizedDateTimePattern;
import static java.time.format.FormatStyle.LONG;
import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toMap;

@Singleton
public class AnswerServiceImpl implements AnswerService {

    private static final Function<AnswerView, Answer> ANSWER = AnswerView::answer;
    private static final Function<AnswerView, LocalDate> GROUP_BY_DATE = ANSWER.andThen(Answer::answerDate);

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
    private final MessageSource messageSource;
    private final MarkdownRenderer markdownRenderer;

    public AnswerServiceImpl(AnswerRepository answerRepository,
                             ProfileRepository profileRepository,
                             MessageSource messageSource,
                             MarkdownRenderer markdownRenderer) {
        this.answerRepository = answerRepository;
        this.profileRepository = profileRepository;
        this.messageSource = messageSource;
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
    public void update(@NotNull Authentication authentication,
                       @NotBlank String questionId,
                       @NotBlank String id,
                       @NotNull @Valid AnswerUpdate answerUpdate,
                       @Nullable Tenant tenant) {
        final AnswerRecord answer = new AnswerRecord(
                id,
                questionId,
                authentication.getName(),
                answerUpdate.answerDate(),
                answerUpdate.format(),
                answerUpdate.content()
        );
        answerRepository.update(answer, tenant);
    }

    @Override
    @NonNull
    public Optional<? extends AnswerView> findById(@NotBlank String id,
                                                   @NotNull Authentication authentication,
                                                   @Nullable Tenant tenant) {
        return answerRepository.findById(id, tenant)
                .map(answer -> buildView(answer, authentication.getName(), tenant));
    }

    @Override
    @NonNull
    public List<DateAnswers> findByQuestionIdGroupedByDate(@NotBlank String questionId,
                                              @NotNull Authentication authentication,
                                              @Nullable Tenant tenant) {
        return dateAnswersByQuestionId(questionId, authentication, tenant);
    }

    @Override
    @NonNull
    public List<? extends AnswerView> findByQuestionId(@NotBlank String questionId,
                                              @NotNull Authentication authentication,
                                              @Nullable Tenant tenant) {
        final Map<String, PublicProfile> respondents = profileRepository.list(tenant).stream().collect(toMap(PublicProfile::id, Function.identity()));
        return answerRepository.findByQuestionId(questionId, tenant).stream()
                .map(answer -> buildView(answer, authentication.getName(), respondents))
                .toList();
    }

    private List<DateAnswers> dateAnswersByQuestionId(@NonNull String id,
                                                      @NonNull Authentication authentication,
                                                      @Nullable Tenant tenant) {
        final List<? extends AnswerView> answers = findByQuestionId(id, authentication, tenant);
        return ViewUtils.encapsulate(answers, GROUP_BY_DATE, reverseOrder(), DateAnswers::new);
    }

    @Override
    @NonNull
    public String getAnswerSummary(@NotNull AnswerView answerView, @Nullable Locale maybeLocale) {
        final PublicProfile respondent = answerView.respondent();
        final LocalDate answerDate = answerView.answer().answerDate();
        final Locale locale = maybeLocale != null ? maybeLocale : Locale.ENGLISH;
        final String pattern = getLocalizedDateTimePattern(LONG, null, answerDate.getChronology(), locale);
        final String when = DateTimeFormatter.ofPattern(pattern, locale).format(answerDate);
        final String who = respondent.fullName().isEmpty() ? respondent.email() : respondent.fullName();
        return messageSource.getMessage("answer.summary", locale, who, when)
                .orElse(who + "'s answer for " + when);
    }

    private AnswerViewRecord buildView(Answer answer, String authenticatedUserId, Tenant tenant) {
        final String respondentId = answer.respondentId();
        final PublicProfile respondent = profileRepository.findById(respondentId, tenant).orElse(null);
        return buildView(answer, authenticatedUserId, Map.of(respondentId, respondent));
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
