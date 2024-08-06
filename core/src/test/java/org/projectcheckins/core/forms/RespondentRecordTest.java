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
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest
class RespondentRecordTest {

    @Test
    void respondentRecordValidation(Validator validator) {
        assertThat(validator.validate(new RespondentRecord(null, null)))
                .hasNotBlankViolation("id")
                .hasNotNullViolation("nextExecution");
        assertThat(validator.validate(new RespondentRecord("", null)))
                .hasNotBlankViolation("id");
        assertThat(validator.validate(new RespondentRecord("id", now())))
                .isValid();
    }

    @Test
    void respondentRecordIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(RespondentRecord.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void respondentRecordIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(RespondentRecord.class)))
            .doesNotThrowAnyException();
    }
}
