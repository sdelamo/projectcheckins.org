package org.projectcheckins.core;

import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.QuestionSave;

@MicronautTest
class QuestionSaveTest {

    @Test
    void titleIsRequired(Validator validator) {
        assertThat(validator.validate(new QuestionSave(null, "schedule")))
            .fieldNotBlank("title");
        assertThat(validator.validate(new QuestionSave("", "schedule")))
            .fieldNotBlank("title");
        assertThat(validator.validate(new QuestionSave("What are you working on", "schedule")))
            .isValid();
    }

    @Test
    void scheduleIsRequired(Validator validator) {
        assertThat(validator.validate(new QuestionSave("What are you working on", null)))
            .fieldNotBlank("schedule");
        assertThat(validator.validate(new QuestionSave("What are you working on", "")))
            .fieldNotBlank("schedule");
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionSave.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionSave.class)))
            .doesNotThrowAnyException();
    }
}
