package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.QuestionUpdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest
class QuestionUpdateTest {

    @Test
    void idCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new QuestionUpdate(null, "What are you working on")))
            .isNotEmpty();
        assertThat(validator.validate(new QuestionUpdate("", "What are you working on")))
            .anyMatch(x -> x.getPropertyPath().toString().equals("id") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new QuestionUpdate("xxx", "What are you working on")))
            .isEmpty();
    }

    @Test
    void titleCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new QuestionUpdate("xxx", null)))
            .isNotEmpty();
        assertThat(validator.validate(new QuestionUpdate("xxx", "")))
            .anyMatch(x -> x.getPropertyPath().toString().equals("title") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new QuestionUpdate("xxx", "What are you working on")))
            .isEmpty();
    }

    @Test
    void questionUpdateIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionUpdate.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionUpdateIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionUpdate.class)))
            .doesNotThrowAnyException();
    }
}
