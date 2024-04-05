package org.projectcheckins.core.forms;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

@Singleton
class OnlyOncePerDayValidator implements ConstraintValidator<OnlyOncePerDay, AnswerForm> {

    private AnswerRepository answerRepository;

    OnlyOncePerDayValidator(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public boolean isValid(AnswerForm value, ConstraintValidatorContext context) {
        // these fields are validated separately, but will make this validator trip if null
        final String questionId = value.questionId();
        final String respondentId = value.respondentId();
        final LocalDate answerDate = value.answerDate();
        final boolean invalid = Stream.of(questionId, respondentId, answerDate).filter(Objects::nonNull).count() < 3;
        return invalid || answerRepository.findByQuestionIdAndRespondentId(questionId, respondentId).stream()
                .noneMatch(a -> answerDate.equals(a.answerDate()));
    }
}
