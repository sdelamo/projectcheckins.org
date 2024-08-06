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

import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerEntityTest {

    @Test
    void settersAndGetters() {
        final LocalDate answerDate = LocalDate.now();
        final AnswerEntity answer = new AnswerEntity();
        answer.id("id");
        answer.questionId("questionId");
        answer.respondentId("respondentId");
        answer.answerDate(answerDate);
        answer.format(Format.MARKDOWN);
        answer.text("text");
        assertThat(answer.id()).isEqualTo("id");
        assertThat(answer.questionId()).isEqualTo("questionId");
        assertThat(answer.respondentId()).isEqualTo("respondentId");
        assertThat(answer.answerDate()).isEqualTo(answerDate);
        assertThat(answer.format()).isEqualTo(Format.MARKDOWN);
        assertThat(answer.text()).isEqualTo("text");
    }
}
