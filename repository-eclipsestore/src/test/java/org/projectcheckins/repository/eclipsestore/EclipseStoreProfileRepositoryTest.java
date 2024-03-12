package org.projectcheckins.repository.eclipsestore;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.security.UserSave;

import java.util.TimeZone;

@MicronautTest
class EclipseStoreProfileRepositoryTest {

  @Test
  void testCrud(EclipseStoreUser storeUser, EclipseStoreProfileRepository profileRepository) throws Exception {
    final String email = "email@example.com";
    final String userId = storeUser.register(new UserSave(
        email, "encodedPassword", emptyList()));
    final Authentication wrongAuth = new ClientAuthentication("wrong-id", null);
    final Authentication rightAuth = new ClientAuthentication(userId, null);
    assertThat(profileRepository.findByAuthentication(rightAuth))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", MONDAY)
            .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWENTY_FOUR_HOUR_CLOCK));

    profileRepository.update(rightAuth, new ProfileUpdate(TimeZone.getDefault(), SUNDAY, TimeFormat.TWELVE_HOUR_CLOCK, "first name", "last name"));
    assertThat(profileRepository.findByAuthentication(rightAuth))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", SUNDAY)
            .hasFieldOrPropertyWithValue("timeFormat", TimeFormat.TWELVE_HOUR_CLOCK)
            .hasFieldOrPropertyWithValue("firstName", "first name")
            .hasFieldOrPropertyWithValue("lastName", "last name"));

    assertThatThrownBy(() -> profileRepository.update(wrongAuth, new ProfileUpdate(TimeZone.getDefault(), SUNDAY, TimeFormat.TWENTY_FOUR_HOUR_CLOCK, "first name", "last name")))
        .isInstanceOf(UserNotFoundException.class);
  }
}
