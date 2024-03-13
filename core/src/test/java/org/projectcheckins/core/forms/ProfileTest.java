package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
@MicronautTest(startApplication = false)
class ProfileTest {

    @Test
    void fieldsCannotBeNull(Validator validator) {
        assertThat(validator.validate(new ProfileRecord("email", null, null, null, null, null, null, null, null)))
                .hasMalformedEmailViolation("email")
                .hasNotNullViolation("timeZone")
                .hasNotNullViolation("firstDayOfWeek")
                .hasNotNullViolation("beginningOfDay")
                .hasNotNullViolation("endOfDay")
                .hasNotNullViolation("timeFormat")
                .hasNotNullViolation("format");
    }

    @Test
    void validProfile(Validator validator) {
        assertThat(validator.validate(new ProfileRecord("delamos@unityfoundation.io", TimeZone.getDefault(), DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(16, 30), TimeFormat.TWENTY_FOUR_HOUR_CLOCK,  Format.MARKDOWN,null, null)))
                .isValid();
    }

    @Test
    void fullName() {
        assertThat(new ProfileRecord("delamos@unityfoundation.io", TimeZone.getDefault(), DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(16, 30), TimeFormat.TWENTY_FOUR_HOUR_CLOCK, Format.MARKDOWN, "Sergio", "del Amo"))
                .extracting(ProfileRecord::fullName)
                .isEqualTo("Sergio del Amo");
    }

    @Test
    void profileIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(ProfileRecord.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void profileIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(ProfileRecord.class)))
                .doesNotThrowAnyException();
    }

}
