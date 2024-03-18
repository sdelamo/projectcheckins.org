package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.TimeOfDay;
import org.projectcheckins.core.forms.QuestionUpdateForm;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Set;

import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest(startApplication = false)
class QuestionUpdateFormTest {
    @Test
    void questionUpdateValidation(Validator validator) {
        Set<ConstraintViolation<QuestionUpdateForm>> violations = validator.validate(new QuestionUpdateForm(null, null, HowOften.DAILY_ON, null, null, null, null, null, null, null));
        assertThat(violations)
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotNullViolation("timeOfDay")
                .hasErrorMessage("You must select at least one day.");
        assertThat(validator.validate(new QuestionUpdateForm("", "", null, null, null, null, null, null, null, null)))
                .hasNotBlankViolation("id")
                .hasNotNullViolation("howOften")
                .hasNotBlankViolation("title");
        assertThat(validator.validate(new QuestionUpdateForm("xxx", "What are you working on", HowOften.DAILY_ON, TimeOfDay.END, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), null, null, null, null, null)))
                .isValid();
        assertThat(validator.validate(new QuestionUpdateForm("xxx", "What are you working on", HowOften.ONCE_A_WEEK, TimeOfDay.END, Collections.emptySet(), DayOfWeek.MONDAY, null, null, null, null)))
                .isValid();
        assertThat(validator.validate(new QuestionUpdateForm("xxx", "What are you working on", HowOften.ONCE_A_WEEK, TimeOfDay.END, null, DayOfWeek.MONDAY, null, null, null, null)))
                .isValid();
    }

    @Test
    void days() {
        assertThat(new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.DAILY_ON, TimeOfDay.END, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), DayOfWeek.MONDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY))
                .extracting(QuestionUpdateForm::days)
                .isEqualTo(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));

        assertThat(new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.ONCE_A_WEEK, TimeOfDay.BEGINNING, Set.of(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY))
                .extracting(QuestionUpdateForm::days)
                .isEqualTo(Set.of(DayOfWeek.TUESDAY));

        assertThat(new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.EVERY_OTHER_WEEK, TimeOfDay.BEGINNING, Set.of(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY))
                .extracting(QuestionUpdateForm::days)
                .isEqualTo(Set.of(DayOfWeek.WEDNESDAY));

        assertThat(new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.ONCE_A_MONTH_ON_THE_FIRST, TimeOfDay.BEGINNING, Set.of(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY))
                .extracting(QuestionUpdateForm::days)
                .isEqualTo(Set.of(DayOfWeek.THURSDAY));

        assertThat(new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", null, TimeOfDay.BEGINNING, Set.of(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY))
                .extracting(QuestionUpdateForm::days)
                .isNotNull();
    }

    @Test
    void daysReturnsNotNull() {
        Assertions.assertThat(new QuestionUpdateForm(null,null,  HowOften.DAILY_ON, null, null, null, null, null, null, null))
                .extracting(QuestionUpdateForm::days)
                .isNotNull();
        Assertions.assertThat(new QuestionUpdateForm(null,null,  HowOften.ONCE_A_WEEK, null, null, null, null, null, null, null))
                .extracting(QuestionUpdateForm::days)
                .isNotNull();
        Assertions.assertThat(new QuestionUpdateForm(null, null,  HowOften.EVERY_OTHER_WEEK, null, null, null, null, null, null, null))
                .extracting(QuestionUpdateForm::days)
                .isNotNull();
        Assertions.assertThat(new QuestionUpdateForm(null,null,  HowOften.ONCE_A_MONTH_ON_THE_FIRST, null, null, null, null, null, null, null))
                .extracting(QuestionUpdateForm::days)
                .isNotNull();
    }

    @Test
    void questionUpdateFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionUpdateForm.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionUpdateFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionUpdateForm.class)))
            .doesNotThrowAnyException();
    }
}
