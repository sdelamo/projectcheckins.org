package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;

@MicronautTest
class EclipseStoreQuestionRepositoryTest {

    @Test
    void testCrud(EclipseStoreQuestionRepository questionRepository) {

        String title = "What are working on?";
        String id = questionRepository.save(new QuestionSave(title));
        assertThat(questionRepository.findAll())
            .anyMatch(q -> q.title().equals(title));

        assertThat(questionRepository.findById(id))
            .isNotEmpty()
            .hasValueSatisfying(q -> q.title().equals(title));

        String updatedTitle = "What are you working on this week?";
        questionRepository.update(new QuestionUpdate(id, updatedTitle));
        assertThat(questionRepository.findById(id))
            .isNotEmpty()
            .hasValueSatisfying(q -> q.title().equals(updatedTitle));

        questionRepository.deleteById(id);
        assertThat(questionRepository.findAll()).isEmpty();
    }
}
