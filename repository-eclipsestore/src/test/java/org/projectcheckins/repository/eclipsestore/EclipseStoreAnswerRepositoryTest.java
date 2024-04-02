package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.AnswerRecord;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class EclipseStoreAnswerRepositoryTest {

    @Test
    void testCrud(EclipseStoreAnswerRepository answerRepository) {
        final AnswerRecord answer = new AnswerRecord(null, "questionId", "user1", LocalDate.now(), Format.MARKDOWN, "Lorem ipsum");
        assertThat(answerRepository.save(answer, null))
                .isNotBlank();
        assertThat(answerRepository.findByQuestionId("questionId", null))
                .isNotEmpty();
    }
}
