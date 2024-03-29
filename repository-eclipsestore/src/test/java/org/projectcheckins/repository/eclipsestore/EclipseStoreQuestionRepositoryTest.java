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
