package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class AnswerSaveTest {
    @Test
    void validation(Validator validator)  {
        assertThat(validator.validate(new AnswerSave(null, null, null,null)))
                .hasNotNullViolation("questionId")
                .hasNotNullViolation("format")
                .hasNotNullViolation("answerDate")
                .hasNotNullViolation("text");

        assertThat(validator.validate(new AnswerSave("", null,  null,"")))
                .hasNotBlankViolation("questionId")
                .hasNotBlankViolation("text");
    }

    @Test
    void answerSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(AnswerSave.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void answerSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(AnswerSave.class)))
                .doesNotThrowAnyException();
    }

}