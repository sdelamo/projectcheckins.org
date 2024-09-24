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

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.security.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreUserPasswordServiceTest {

    @Test
    void updatePassword(EclipseStoreUser eclipseStoreUser, RootProvider<Data> rootProvider, PasswordEncoder passwordEncoder) {
        final String userId = "user1";
        final String newPassword = "new-password";
        rootProvider.root().getUsers().add(new UserEntity(
                userId,
                "user1@email.com",
                "old password",
                Collections.emptyList(),
                TimeZone.getDefault(),
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(16, 30),
                TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
                Format.MARKDOWN,
                null,
                null));
        eclipseStoreUser.updatePassword(userId, newPassword);
        assertThat(eclipseStoreUser.findById(userId))
                .hasValueSatisfying(u -> passwordEncoder.matches(newPassword, u.getPassword()));
    }

    @Test
    void updatePasswordNoSuchUser(PasswordService passwordService) {
        final String userId = "no-such-user";
        final String newPassword = "new-password";
        assertThatCode(() -> passwordService.updatePassword(userId, newPassword))
                .doesNotThrowAnyException();
    }
}
