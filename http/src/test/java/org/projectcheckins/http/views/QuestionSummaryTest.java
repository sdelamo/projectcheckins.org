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

package org.projectcheckins.http.views;

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.ViewsRenderer;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.api.Respondent;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.QuestionRecord;
import org.projectcheckins.core.forms.RespondentRecord;
import org.projectcheckins.core.forms.TimeOfDay;
import org.projectcheckins.test.WritableUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.projectcheckins.core.forms.HowOften.*;
import static org.projectcheckins.core.forms.TimeOfDay.*;

@MicronautTest(startApplication = false)
class QuestionSummaryTest {

    static final LocalTime TEN_SHARP = LocalTime.of(10, 0);
    static final LocalTime TWELVE_THIRTY = LocalTime.of(12, 30);
    static final Respondent RESPONDENT_1 = new RespondentRecord("user1", now());
    static final Respondent RESPONDENT_2 = new RespondentRecord("user2", now());
    static final Set<Respondent> ONE_PERSON = Set.of(RESPONDENT_1);
    static final Set<Respondent> TWO_PEOPLE = Set.of(RESPONDENT_1, RESPONDENT_2);

    @Inject
    ViewsRenderer<Map<String, Object>, HttpRequest<?>> viewsRenderer;

    static Question question(Set<Respondent> respondents, HowOften howOften, Set<DayOfWeek> days, TimeOfDay timeOfDay, LocalTime fixedTime) {
        return new QuestionRecord("id", "title", howOften, days, timeOfDay, fixedTime, respondents);
    }

    @Test
    void askingOnePersonEveryMondayAtTheBeginningOfTheDay(ViewsRenderer<Map<String, Object>, HttpRequest<?>> viewsRenderer) {
        assertThat(render(question(ONE_PERSON, DAILY_ON, Set.of(MONDAY), BEGINNING, TWELVE_THIRTY)))
                .containsIgnoringWhitespaces("Asking 1 person every Monday at the beginning of the day.");
    }

    @Test
    void askingTwoPeopleEveryTuesdayAndWednesdayAtTheEndOfTheDay() {
        assertThat(render(question(TWO_PEOPLE, DAILY_ON, Set.of(TUESDAY, WEDNESDAY), END, TWELVE_THIRTY)))
                .containsIgnoringWhitespaces("Asking 2 people every Tuesday and Wednesday at the end of the day.");
    }

    @Test
    void askingOnePersonEveryWeekdayAtTenSharp() {
        assertThat(render(question(ONE_PERSON, DAILY_ON, Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY), FIXED, TEN_SHARP)))
                .containsIgnoringWhitespaces("Asking 1 person every weekday at 10:00.");
    }

    @Test
    void askingOnePersonEveryThursdayAtTheBeginningOfTheDay() {
        assertThat(render(question(ONE_PERSON, ONCE_A_WEEK, Set.of(THURSDAY), BEGINNING, TEN_SHARP)))
                .containsIgnoringWhitespaces("Asking 1 person every Thursday at the beginning of the day.");
    }

    @Test
    void askingTwoPeopleEveryOtherFridayAtTheEndOfTheDay() {
        assertThat(render(question(TWO_PEOPLE, EVERY_OTHER_WEEK, Set.of(FRIDAY), END, TWELVE_THIRTY)))
                .containsIgnoringWhitespaces("Asking 2 people every other Friday at the end of the day.");
    }

    @Test
    void askingOnePersonTheFirstSaturdayOfEachMonthAtTwelveThirty() {
        assertThat(render(question(ONE_PERSON, ONCE_A_MONTH_ON_THE_FIRST, Set.of(SATURDAY), FIXED, TWELVE_THIRTY)))
                .containsIgnoringWhitespaces("Asking 1 person the first Saturday of each month at 12:30.");
    }

    String render(Question question) {
        final Writable writable = viewsRenderer.render("question/_summary.html", Map.of("question", question), null);
        final Optional<String> summary = WritableUtils.writableToString(writable);
        assertThat(summary).isNotEmpty();
        return summary.orElseThrow();
    }
}
