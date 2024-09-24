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

package org.projectcheckins.core.models;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class DateAnswersTest {
    private static final Profile USER_1 = new ProfileRecord(
            "user1",
            "user1@email.com",
            TimeZone.getDefault(),
            DayOfWeek.MONDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
            Format.MARKDOWN,
            "Code",
            "Monkey",
            true
    );
    private static final Answer ANSWER_1 = new AnswerRecord("answer1", "question1", USER_1.id(), LocalDate.of(2024, 1, 1), Format.MARKDOWN, "Lorem *ipsum*.");

    @Test
    void validation(Validator validator)  {
        assertThat(validator.validate(new DateAnswers(LocalDate.now().plusDays(1), Collections.emptyList())))
                .hasNotEmptyViolation("answers");

        assertThat(validator.validate(new DateAnswers(LocalDate.now(),  Collections.singletonList(new AnswerViewRecord(ANSWER_1, USER_1, "Lorem <em>ipsum</em>.", true)))))
                .isValid();
    }

    @Test
    void dateAnswersIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(DateAnswers.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void dateAnswersIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(DateAnswers.class)))
                .doesNotThrowAnyException();
    }
}
