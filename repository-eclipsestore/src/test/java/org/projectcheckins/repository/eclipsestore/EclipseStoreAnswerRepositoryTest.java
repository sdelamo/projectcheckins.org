package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.forms.AnswerRecord;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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
    }
}
