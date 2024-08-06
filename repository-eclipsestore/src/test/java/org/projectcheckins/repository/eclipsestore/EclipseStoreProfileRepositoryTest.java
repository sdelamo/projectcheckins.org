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

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.security.Role.ROLE_ADMIN;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.security.UserSave;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@MicronautTest
class EclipseStoreProfileRepositoryTest {

  @Test
  void testCrud(EclipseStoreUser storeUser, EclipseStoreProfileRepository profileRepository) throws Exception {
    final String email = "email@example.com";
    List<String> authorities = emptyList();
    final String userId = storeUser.register(new UserSave(
        email, "encodedPassword", authorities), null);
    final Authentication wrongAuth = new ClientAuthentication("wrong-id", null);
    final Authentication rightAuth = new ClientAuthentication(userId, null);
    assertThat(profileRepository.findByAuthentication(rightAuth))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", MONDAY)
            .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWENTY_FOUR_HOUR_CLOCK));

    profileRepository.update(rightAuth, new ProfileUpdate(TimeZone.getDefault(), SUNDAY, LocalTime.of(9, 0), LocalTime.of(16, 30), TimeFormat.TWELVE_HOUR_CLOCK, Format.WYSIWYG, "first name", "last name"));
    assertThat(profileRepository.findByAuthentication(rightAuth))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("admin", false)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", SUNDAY)
            .hasFieldOrPropertyWithValue("beginningOfDay", LocalTime.of(9, 0))
            .hasFieldOrPropertyWithValue("endOfDay", LocalTime.of(16, 30))
            .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWELVE_HOUR_CLOCK)
            .hasFieldOrPropertyWithValue("format", Format.WYSIWYG)
            .hasFieldOrPropertyWithValue("firstName", "first name")
            .hasFieldOrPropertyWithValue("lastName", "last name"));

    profileRepository.updateAuthorities(email, Collections.singletonList(ROLE_ADMIN), null);

    assertThat(profileRepository.findByAuthentication(rightAuth))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("admin", true));

    assertThatThrownBy(() -> profileRepository.update(wrongAuth, new ProfileUpdate(TimeZone.getDefault(), SUNDAY, LocalTime.of(9, 0), LocalTime.of(16, 30), TimeFormat.TWENTY_FOUR_HOUR_CLOCK,  Format.MARKDOWN,"first name", "last name")))
        .isInstanceOf(UserNotFoundException.class);

    assertThatCode(() -> profileRepository.deleteByEmail(email, null))
        .doesNotThrowAnyException();

    assertThatThrownBy(() -> profileRepository.deleteByEmail(email, null))
        .isInstanceOf(UserNotFoundException.class);
  }
}
