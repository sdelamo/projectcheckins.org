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

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.repositories.SecondaryAnswerRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.core.forms.Format.MARKDOWN;

@Property(name = "spec.name", value = "OnlyOncePerDayValidatorTest")
@MicronautTest(startApplication = false)
class OnlyOncePerDayValidatorTest {

    static final String QUESTION_1 = "question1";
    static final String USER_1 = "user1";
    static final LocalDate JANUARY_1 = LocalDate.of(2024, 1, 1);
    static final LocalDate JANUARY_2 = LocalDate.of(2024, 1, 2);
    static final String TEXT = "Lorem ipsum";

    @Test
    void userAlreadyAnswered(OnlyOncePerDayValidator onlyOncePerDayValidator) {
        AnswerForm answerForm = new AnswerMarkdownSave(QUESTION_1, USER_1, JANUARY_1, TEXT);
        assertThat(onlyOncePerDayValidator.isValid(answerForm, null))
                .isFalse();
    }

    @Test
    void userDidNotAnswerYet(OnlyOncePerDayValidator onlyOncePerDayValidator) {
        AnswerForm answerForm = new AnswerMarkdownSave(QUESTION_1, USER_1, JANUARY_2, TEXT);
        assertThat(onlyOncePerDayValidator.isValid(answerForm, null))
                .isTrue();
    }

    @Requires(property = "spec.name", value = "OnlyOncePerDayValidatorTest")
    @Singleton
    static class AnswerRepositoryMock extends SecondaryAnswerRepository {

        private final List<Answer> answers = Collections.singletonList(
                new AnswerRecord("answer1", QUESTION_1, USER_1, JANUARY_1, MARKDOWN, TEXT)
        );

        @Override
        public List<? extends Answer> findByQuestionIdAndRespondentId(String questionId, String respondentId) {
            return answers.stream().filter(a -> a.questionId().equals(questionId) && a.respondentId().equals(respondentId)).toList();
        }
    }
}
