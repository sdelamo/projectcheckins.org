package org.projectcheckins.core;

import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.QuestionRecord;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

@MicronautTest
class QuestionRecordTest {

    @Test
    void questionRecordValidation(Validator validator) {
        assertThat(validator.validate(new QuestionRecord(null, null, null, null, null, null)))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("days")
                .hasNotNullViolation("timeOfDay")
                .hasNotNullViolation("fixedTime");
        assertThat(validator.validate(new QuestionRecord("", "", null, Collections.emptySet(), null, null)))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotEmptyViolation("days");
        assertThat(validator.validate(new QuestionRecord("xxx", "What are you working on", HowOften.DAILY_ON, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END, LocalTime.of(16, 30))))
                .isValid();
    }

    @Test
    void questionRecordIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionRecord.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionRecordIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionRecord.class)))
            .doesNotThrowAnyException();
    }
}
