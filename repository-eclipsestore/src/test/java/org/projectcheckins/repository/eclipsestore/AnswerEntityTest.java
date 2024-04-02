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
