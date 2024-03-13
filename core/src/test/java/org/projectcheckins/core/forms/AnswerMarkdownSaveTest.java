package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class AnswerMarkdownSaveTest {

    @Test
    void validation(Validator validator)  {
        assertThat(validator.validate(new AnswerMarkdownSave(null, null, null)))
                .hasNotNullViolation("questionId")
                .hasNotNullViolation("answerDate")
                .hasNotNullViolation("markdown");

        assertThat(validator.validate(new AnswerMarkdownSave("", null, "")))
                .hasNotBlankViolation("questionId")
                .hasNotBlankViolation("markdown");
    }

    @Test
    void answerMarkdownSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(AnswerMarkdownSave.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void answerMarkdownSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(AnswerMarkdownSave.class)))
                .doesNotThrowAnyException();
    }

}