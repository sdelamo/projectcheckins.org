// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class AnswerWyswygSaveTest {

    @Test
    void validation(Validator validator)  {
        assertThat(validator.validate(new AnswerWysiwygSave(null, null, null, null)))
                .hasNotNullViolation("questionId")
                .hasNotNullViolation("respondentId")
                .hasNotNullViolation("answerDate")
                .hasNotNullViolation("html");

        assertThat(validator.validate(new AnswerWysiwygSave("", "", null, "")))
                .hasNotBlankViolation("questionId")
                .hasNotNullViolation("respondentId")
                .hasNotBlankViolation("html");
    }

    @Test
    void answerWysiwygSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(AnswerWysiwygSave.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void answerWysiwygSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(AnswerWysiwygSave.class)))
                .doesNotThrowAnyException();
    }

}