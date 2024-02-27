package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionUpdate;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class QuestionUpdateTest {

    @Test
    void idCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new QuestionUpdate(null, "What are you working on")).isEmpty());
        assertFalse(validator.validate(new QuestionUpdate("", "What are you working on")).isEmpty());
        assertTrue(validator.validate(new QuestionUpdate("xxx", "What are you working on")).isEmpty());
    }

    @Test
    void titleCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new QuestionUpdate("xxx", null)).isEmpty());
        assertFalse(validator.validate(new QuestionUpdate("xxx", "")).isEmpty());
        assertTrue(validator.validate(new QuestionUpdate("xxx", "What are you working on")).isEmpty());
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionUpdate.class)));
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Question.class)));
    }
}
