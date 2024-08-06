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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.TimeFormat;

import java.util.Collections;

class UserEntityTest {

    @Test
    void settersAndGetters() {
        UserEntity user = new UserEntity(
                "id",
                "email@projectcheckins.org",
                "password",
                Collections.emptyList(),
                TimeZone.getDefault(),
                DayOfWeek.SUNDAY,
                LocalTime.of(9, 0),
                LocalTime.of(16, 30),
                TimeFormat.TWELVE_HOUR_CLOCK,
                Format.MARKDOWN,
                "first name",
                "last name"
        );
        assertThat(user)
                .hasFieldOrPropertyWithValue("id", "id")
                .hasFieldOrPropertyWithValue("email", "email@projectcheckins.org")
                .hasFieldOrPropertyWithValue("encodedPassword", "password")
                .hasFieldOrPropertyWithValue("enabled", false)
                .satisfies(u -> assertThat(u.authorities()).isEmpty())
                .hasFieldOrPropertyWithValue("timeZone", TimeZone.getDefault())
                .hasFieldOrPropertyWithValue("firstDayOfWeek", DayOfWeek.SUNDAY)
                .hasFieldOrPropertyWithValue("beginningOfDay", LocalTime.of(9, 0))
                .hasFieldOrPropertyWithValue("endOfDay", LocalTime.of(16, 30))
                .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWELVE_HOUR_CLOCK)
                .hasFieldOrPropertyWithValue("format", Format.MARKDOWN)
                .hasFieldOrPropertyWithValue("firstName", "first name")
                .hasFieldOrPropertyWithValue("lastName", "last name");

        user.id("newId");
        user.email("newEmail@projectcheckins.org");
        user.enabled(true);
        user.encodedPassword("newPassword");
        user.authorities(Collections.singletonList("ROLE_USER"));
        user.timeZone(TimeZone.getTimeZone("Europe/Madrid"));
        user.firstDayOfWeek(DayOfWeek.MONDAY);
        user.beginningOfDay(LocalTime.of(8, 0));
        user.endOfDay(LocalTime.of(17, 30));
        user.timeFormat(TimeFormat.TWENTY_FOUR_HOUR_CLOCK);
        user.format(Format.WYSIWYG);
        user.firstName("FIRST NAME");
        user.lastName("LAST NAME");

        assertThat(user)
                .hasFieldOrPropertyWithValue("id", "newId")
                .hasFieldOrPropertyWithValue("email", "newEmail@projectcheckins.org")
                .hasFieldOrPropertyWithValue("encodedPassword", "newPassword")
                .hasFieldOrPropertyWithValue("enabled", true)
                .satisfies(u -> assertThat(u.authorities()).containsExactly("ROLE_USER"))
                .hasFieldOrPropertyWithValue("timeZone", TimeZone.getTimeZone("Europe/Madrid"))
                .hasFieldOrPropertyWithValue("firstDayOfWeek", DayOfWeek.MONDAY)
                .hasFieldOrPropertyWithValue("beginningOfDay", LocalTime.of(8, 0))
                .hasFieldOrPropertyWithValue("endOfDay", LocalTime.of(17, 30))
                .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWENTY_FOUR_HOUR_CLOCK)
                .hasFieldOrPropertyWithValue("format", Format.WYSIWYG)
                .hasFieldOrPropertyWithValue("firstName", "FIRST NAME")
                .hasFieldOrPropertyWithValue("lastName", "LAST NAME");
    }

}
