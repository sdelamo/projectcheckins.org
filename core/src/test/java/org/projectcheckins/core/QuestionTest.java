package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class QuestionTest {

    @Test
    void idCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new Question(null, "What are you working on")).isEmpty());
        assertFalse(validator.validate(new Question("", "What are you working on")).isEmpty());
        assertTrue(validator.validate(new Question("xxx", "What are you working on")).isEmpty());
    }

    @Test
    void titleCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new Question("xxx", null)).isEmpty());
        assertFalse(validator.validate(new Question("xxx", "")).isEmpty());
        assertTrue(validator.validate(new Question("xxx", "What are you working on")).isEmpty());
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Question.class)));
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Question.class)));
    }
}
