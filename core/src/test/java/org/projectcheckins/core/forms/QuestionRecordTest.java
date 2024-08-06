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

import static java.time.ZonedDateTime.now;
import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.validation.groups.Default;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

@MicronautTest
class QuestionRecordTest {

    @Test
    void questionRecordValidation(Validator validator) {
        final Class<?>[] SAVED = {Default.class, Saved.class};
        assertThat(validator.validate(new QuestionRecord(null, null, null, null, null, null, null), SAVED))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("days")
                .hasNotNullViolation("timeOfDay")
                .hasNotNullViolation("fixedTime")
                .hasNotNullViolation("respondents");
        assertThat(validator.validate(new QuestionRecord(null, "", null, Collections.emptySet(), null, null, Collections.emptySet()), SAVED))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotEmptyViolation("days")
                .hasNotEmptyViolation("respondents");
        assertThat(validator.validate(new QuestionRecord("xxx", "What are you working on", HowOften.DAILY_ON, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END, LocalTime.of(16, 30), Set.of(new RespondentRecord("id", now()))), SAVED))
                .isValid();
        final Class<?>[] NOT_SAVED = {};
        assertThat(validator.validate(new QuestionRecord(null, null, null, null, null, null, null), NOT_SAVED))
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("days")
                .hasNotNullViolation("timeOfDay")
                .hasNotNullViolation("fixedTime")
                .hasNotNullViolation("respondents");
        assertThat(validator.validate(new QuestionRecord(null, "", null, Collections.emptySet(), null, null, Collections.emptySet()), NOT_SAVED))
                .hasNotBlankViolation("title")
                .hasNotEmptyViolation("days")
                .hasNotEmptyViolation("respondents");
        assertThat(validator.validate(new QuestionRecord(null, "What are you working on", HowOften.DAILY_ON, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END, LocalTime.of(16, 30), Set.of(new RespondentRecord("id", now()))), NOT_SAVED))
                .isValid();
    }

    @Test
    void questionRecordIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionRecord.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionRecordIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionRecord.class)))
            .doesNotThrowAnyException();
    }
}
