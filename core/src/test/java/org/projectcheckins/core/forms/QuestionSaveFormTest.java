package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class QuestionSaveFormTest {
    @Test
    void questionSaveFormValidation(Validator validator) {
        assertThat(validator.validate(new QuestionSaveForm("What did you do today, and what will you work on tomorrow?", HowOften.DAILY_ON, TimeOfDay.END, LocalTime.of(16, 30), Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), DayOfWeek.MONDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY, Set.of("user1"))))
                .isValid();
        assertThat(validator.validate(new QuestionSaveForm(null, null, null, null, null, null, null, null, null, null, null)))
                .hasErrorMessage("You must select at least one day.")
                .hasErrorMessage("You must select at least one respondent.")
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("timeOfDay")
                .hasNotNullViolation("fixedTime");

        assertThat(validator.validate(new QuestionSaveForm("", null, null, null, Collections.emptySet(), null, null, null, Collections.emptySet(), null, null)))
                .hasErrorMessage("You must select at least one day.")
                .hasErrorMessage("You must select at least one respondent.")
                .hasNotBlankViolation("title");

    }

    @Test
    void questionSaveFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionSaveForm.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void questionSaveFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionSaveForm.class)))
                .doesNotThrowAnyException();
    }
}
