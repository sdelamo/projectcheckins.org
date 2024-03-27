package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.RespondentRecord;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest
class RespondentRecordTest {

    @Test
    void respondentRecordValidation(Validator validator) {
        assertThat(validator.validate(new RespondentRecord(null)))
                .hasNotBlankViolation("id");
        assertThat(validator.validate(new RespondentRecord("")))
                .hasNotBlankViolation("id");
        assertThat(validator.validate(new RespondentRecord("PROFILE ID")))
                .isValid();
    }

    @Test
    void respondentRecordIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(RespondentRecord.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void respondentRecordIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(RespondentRecord.class)))
            .doesNotThrowAnyException();
    }
}
