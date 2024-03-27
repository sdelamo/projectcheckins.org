package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Introspected
class NotEmptyRespondentsValidator implements ConstraintValidator<NotEmptyRespondents, QuestionForm> {
    @Override
    public boolean isValid(QuestionForm value, ConstraintValidatorContext context) {
        return !value.respondents().isEmpty();
    }
}
