package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.*;

@MicronautTest(startApplication = false)
class ProfileTest {

    @Test
    void fieldsCannotBeNull(Validator validator) {
        assertThat(validator.validate(new Profile("email", null, null, null)))
                .hasMalformedEmailViolation("email")
                .hasNotNullViolation("timeZone")
                .hasNotNullViolation("firstDayOfWeek")
                .hasNotNullViolation("timeFormat");
    }

    @Test
    void profileIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Profile.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void profileIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Profile.class)))
                .doesNotThrowAnyException();
    }

}