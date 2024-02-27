package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class EclipseStoreQuestionRepositoryTest {

    @Test
    void testCrud(EclipseStoreQuestionRepository questionRepository) {

        String id = questionRepository.save(new QuestionSave("What are working on?"));
        Optional<Question> questionOptional = questionRepository.findAll()
                .stream()
                .filter(q -> q.title().equals("What are working on?"))
                .findFirst();
        assertTrue(questionOptional.isPresent());
        Question question = questionOptional.get();
        assertEquals("What are working on?", question.title());
        questionOptional =  questionRepository.findById(id);
        assertTrue(questionOptional.isPresent());
        question = questionOptional.get();
        assertEquals("What are working on?", question.title());

        String updatedTitle = "What are you working on this week?";
        questionRepository.update(new QuestionUpdate(id, updatedTitle));
        questionOptional =  questionRepository.findById(id);
        assertTrue(questionOptional.isPresent());
        question = questionOptional.get();
        assertEquals(updatedTitle, question.title());
        questionRepository.deleteById(question.id());
        assertTrue(questionRepository.findAll().isEmpty());
    }
}
