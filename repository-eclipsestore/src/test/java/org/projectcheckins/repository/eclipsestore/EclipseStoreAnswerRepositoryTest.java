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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.exceptions.AnswerNotFoundException;
import org.projectcheckins.core.forms.AnswerRecord;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MicronautTest
class EclipseStoreAnswerRepositoryTest {

    @Test
    void testCrud(EclipseStoreAnswerRepository answerRepository) {
        final LocalDate answerDate = LocalDate.now();
        final String questionId = "question1";
        final String respondentId = "user1";
        final String text = "Lorem ipsum";
        final AnswerRecord answer = new AnswerRecord(null, questionId, respondentId, answerDate, Format.MARKDOWN, text);
        final String id = answerRepository.save(answer, null);
        final ThrowingConsumer<? super Answer> expectedId = a -> assertThat(a).hasFieldOrPropertyWithValue("id", id);
        assertThat(id)
                .isNotBlank();
        assertThat(answerRepository.findById(id, null))
                .hasValueSatisfying(a -> assertThat(a)
                        .hasFieldOrPropertyWithValue("id", id)
                        .hasFieldOrPropertyWithValue("questionId", questionId)
                        .hasFieldOrPropertyWithValue("respondentId", respondentId)
                        .hasFieldOrPropertyWithValue("format", Format.MARKDOWN)
                        .hasFieldOrPropertyWithValue("text", text));
        assertThat(answerRepository.findByQuestionId(questionId, null))
                .satisfiesOnlyOnce(expectedId);
        assertThat(answerRepository.findByQuestionIdAndRespondentId(questionId, respondentId))
                .satisfiesOnlyOnce(expectedId);
        assertThat(answerRepository.findByQuestionIdAndRespondentId("question2", respondentId))
                .isEmpty();
        assertThat(answerRepository.findByQuestionIdAndRespondentId(questionId, "user2"))
                .isEmpty();
        final String updatedText = "Hello world";
        final AnswerRecord answerUpdate = new AnswerRecord(id, questionId, respondentId, answerDate, Format.MARKDOWN, updatedText);
        assertThatCode(() -> answerRepository.update(answerUpdate, null));
        assertThat(answerRepository.findById(id, null))
                .hasValueSatisfying(a -> assertThat(a).hasFieldOrPropertyWithValue("text", updatedText));
        final AnswerRecord wrongAnswer = new AnswerRecord("404", questionId, respondentId, answerDate, Format.MARKDOWN, updatedText);
        assertThatThrownBy(() -> answerRepository.update(wrongAnswer, null))
                .isInstanceOf(AnswerNotFoundException.class);
    }
}
