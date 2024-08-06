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

package org.projectcheckins.repository.eclipsestore;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.QuestionRecord;
import org.projectcheckins.core.forms.RespondentRecord;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@MicronautTest
class EclipseStoreQuestionRepositoryTest {

    @Test
    void testCrud(EclipseStoreQuestionRepository questionRepository) {

        String title = "What are working on?";
        String id = questionRepository.save(new QuestionRecord(null, title, HowOften.DAILY_ON,
                Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END, LocalTime.of(16, 30), Set.of(new RespondentRecord("id", now()))
                ));
        assertThat(questionRepository.findAll())
            .anyMatch(q -> q.title().equals(title));

        assertThat(questionRepository.findById(id))
            .isNotEmpty()
            .hasValueSatisfying(q -> q.title().equals(title));

        assertThatThrownBy(() -> questionRepository.update(new QuestionRecord(null, title, HowOften.DAILY_ON,
                Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END, LocalTime.of(16, 30), Set.of(new RespondentRecord("id", now())))))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageEndingWith("id: must not be blank");

        String updatedTitle = "What are you working on this week?";
        questionRepository.update(new QuestionRecord(id, updatedTitle, HowOften.DAILY_ON,
                Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END, LocalTime.of(16, 30), Set.of(new RespondentRecord("id", now()))
                ));
        assertThat(questionRepository.findById(id))
            .isNotEmpty()
            .hasValueSatisfying(q -> q.title().equals(updatedTitle));

        questionRepository.deleteById(id);
        assertThat(questionRepository.findAll()).isEmpty();
    }
}
