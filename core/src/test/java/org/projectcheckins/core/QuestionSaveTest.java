package org.projectcheckins.core;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(validator.validate(new QuestionSave(null)))
            .isNotEmpty();
        assertThat(validator.validate(new QuestionSave("")))
            .anyMatch(x -> x.getPropertyPath().toString().equals("title") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new QuestionSave("What are you working on")))
            .isEmpty();
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
