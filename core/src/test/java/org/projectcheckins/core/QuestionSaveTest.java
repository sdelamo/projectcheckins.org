package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.QuestionSave;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class QuestionSaveTest {

    @Test
    void titleIsRequired(Validator validator) {
        assertFalse(validator.validate(new QuestionSave(null)).isEmpty());
        assertFalse(validator.validate(new QuestionSave("")).isEmpty());
        assertTrue(validator.validate(new QuestionSave("What are you working on")).isEmpty());
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionSave.class)));
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionSave.class)));
    }
}
