package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.QuestionSaveForm;
import org.projectcheckins.core.forms.QuestionUpdateForm;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@MicronautTest
class EclipseStoreQuestionRepositoryTest {

    @Test
    void testCrud(EclipseStoreQuestionRepository questionRepository) {

        String title = "What are working on?";
        String id = questionRepository.save(new QuestionSaveForm(title, HowOften.DAILY_ON, TimeOfDay.END, LocalTime.of(16, 30),
                Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), null, null, null
                ));
        assertThat(questionRepository.findAll())
            .anyMatch(q -> q.title().equals(title));

        assertThat(questionRepository.findById(id))
            .isNotEmpty()
            .hasValueSatisfying(q -> q.title().equals(title));

        String updatedTitle = "What are you working on this week?";
        questionRepository.update(new QuestionUpdateForm(id, updatedTitle, HowOften.DAILY_ON, TimeOfDay.END, LocalTime.of(16, 30),
                Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), null, null, null
                ));
        assertThat(questionRepository.findById(id))
            .isNotEmpty()
            .hasValueSatisfying(q -> q.title().equals(updatedTitle));

        questionRepository.deleteById(id);
        assertThat(questionRepository.findAll()).isEmpty();
    }
}
