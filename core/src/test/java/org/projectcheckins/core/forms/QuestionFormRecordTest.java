package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class QuestionFormRecordTest {
    @Test
    void questionFormValidation(Validator validator) {
        assertThat(validator.validate(new QuestionFormRecord(null, null, null, null, null, null, null, null, null, null, null)))
                .hasErrorMessage("You must select at least one day.")
                .hasErrorMessage("You must select at least one respondent.")
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("timeOfDay")
                .hasNotNullViolation("fixedTime");
        assertThat(validator.validate(new QuestionFormRecord("", null, null, null, Collections.emptySet(), null, null, null, Collections.emptySet(), null, null)))
                .hasErrorMessage("You must select at least one day.")
                .hasErrorMessage("You must select at least one respondent.")
                .hasNotBlankViolation("title");
        assertThat(validator.validate(new QuestionFormRecord("What did you do today, and what will you work on tomorrow?", HowOften.DAILY_ON, TimeOfDay.END, LocalTime.of(16, 30), Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), DayOfWeek.MONDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY, Set.of("user1"), null, null)))
                .isValid();
    }

    @Test
    void daysReturnsNotNull() {
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.DAILY_ON, null, null, null, null, null, null, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .isEmpty();
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.ONCE_A_WEEK, null, null, null, null, null, null, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .isEmpty();
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.EVERY_OTHER_WEEK, null, null, null, null, null, null, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .isEmpty();
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.ONCE_A_MONTH_ON_THE_FIRST, null, null, null, null, null, null, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .isEmpty();
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.DAILY_ON, null, null, Set.of(DayOfWeek.FRIDAY), null, null, null, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .containsExactly(DayOfWeek.FRIDAY);
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.ONCE_A_WEEK, null, null, null, DayOfWeek.FRIDAY, null, null, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .containsExactly(DayOfWeek.FRIDAY);
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.EVERY_OTHER_WEEK, null, null, null, null, DayOfWeek.FRIDAY, null, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .containsExactly(DayOfWeek.FRIDAY);
        Assertions.assertThat(new QuestionFormRecord(null, HowOften.ONCE_A_MONTH_ON_THE_FIRST, null, null, null, null, null, DayOfWeek.FRIDAY, null, null, null))
                .extracting(QuestionFormRecord::days, COLLECTION)
                .containsExactly(DayOfWeek.FRIDAY);
    }

    @Test
    void ofQuestion() {
        final String title = "TITLE";
        final LocalTime fixedTime = LocalTime.of(12, 30);
        Assertions.assertThat(QuestionFormRecord.of(new QuestionRecord(null, title, HowOften.DAILY_ON, Set.of(DayOfWeek.FRIDAY), TimeOfDay.FIXED, fixedTime, Set.of(new RespondentRecord("user1", now())))))
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("howOften", HowOften.DAILY_ON)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.FIXED)
                .hasFieldOrPropertyWithValue("fixedTime", fixedTime)
                .hasFieldOrPropertyWithValue("dailyOnDay", Set.of(DayOfWeek.FRIDAY))
                .hasFieldOrPropertyWithValue("onceAWeekDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("everyOtherWeekDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("onceAMonthOnTheFirstDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("respondentIds", Set.of("user1"))
                .hasFieldOrPropertyWithValue("fieldErrors", null)
                .hasFieldOrPropertyWithValue("errors", null);
        Assertions.assertThat(QuestionFormRecord.of(new QuestionRecord(null, title, HowOften.ONCE_A_WEEK, Set.of(DayOfWeek.FRIDAY), TimeOfDay.FIXED, fixedTime, Set.of(new RespondentRecord("user1", now())))))
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("howOften", HowOften.ONCE_A_WEEK)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.FIXED)
                .hasFieldOrPropertyWithValue("fixedTime", fixedTime)
                .hasFieldOrPropertyWithValue("dailyOnDay", Set.of(DayOfWeek.MONDAY))
                .hasFieldOrPropertyWithValue("onceAWeekDay", DayOfWeek.FRIDAY)
                .hasFieldOrPropertyWithValue("everyOtherWeekDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("onceAMonthOnTheFirstDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("respondentIds", Set.of("user1"))
                .hasFieldOrPropertyWithValue("fieldErrors", null)
                .hasFieldOrPropertyWithValue("errors", null);
        Assertions.assertThat(QuestionFormRecord.of(new QuestionRecord(null, title, HowOften.EVERY_OTHER_WEEK, Set.of(DayOfWeek.FRIDAY), TimeOfDay.FIXED, fixedTime, Set.of(new RespondentRecord("user1", now())))))
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("howOften", HowOften.EVERY_OTHER_WEEK)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.FIXED)
                .hasFieldOrPropertyWithValue("fixedTime", fixedTime)
                .hasFieldOrPropertyWithValue("dailyOnDay", Set.of(DayOfWeek.MONDAY))
                .hasFieldOrPropertyWithValue("onceAWeekDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("everyOtherWeekDay", DayOfWeek.FRIDAY)
                .hasFieldOrPropertyWithValue("onceAMonthOnTheFirstDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("respondentIds", Set.of("user1"))
                .hasFieldOrPropertyWithValue("fieldErrors", null)
                .hasFieldOrPropertyWithValue("errors", null);
        Assertions.assertThat(QuestionFormRecord.of(new QuestionRecord(null, title, HowOften.ONCE_A_MONTH_ON_THE_FIRST, Set.of(DayOfWeek.FRIDAY), TimeOfDay.FIXED, fixedTime, Set.of(new RespondentRecord("user1", now())))))
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("howOften", HowOften.ONCE_A_MONTH_ON_THE_FIRST)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.FIXED)
                .hasFieldOrPropertyWithValue("fixedTime", fixedTime)
                .hasFieldOrPropertyWithValue("dailyOnDay", Set.of(DayOfWeek.MONDAY))
                .hasFieldOrPropertyWithValue("onceAWeekDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("everyOtherWeekDay", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("onceAMonthOnTheFirstDay", DayOfWeek.FRIDAY)
                .hasFieldOrPropertyWithValue("respondentIds", Set.of("user1"))
                .hasFieldOrPropertyWithValue("fieldErrors", null)
                .hasFieldOrPropertyWithValue("errors", null);
    }

    @Test
    void questionFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionFormRecord.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void questionFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionFormRecord.class)))
                .doesNotThrowAnyException();
    }
}
