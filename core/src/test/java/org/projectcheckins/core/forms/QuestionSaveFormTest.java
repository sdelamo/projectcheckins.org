package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class QuestionSaveFormTest {
    @Test
    void questionSaveFormValidation(Validator validator) {
        assertThat(validator.validate(new QuestionSaveForm("What did you do today, and what will you work on tomorrow?")))
                .isValid();
        assertThat(validator.validate(new QuestionSaveForm(null, null, null, null, null, null, null, null, null, null)))
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("timeOfDay")
                .hasNotNullViolation("fixedTime");
        assertThat(validator.validate(new QuestionSaveForm("", null, null, null, null, null, null, null, null, null)))
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