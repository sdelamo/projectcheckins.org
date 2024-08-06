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

import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.UserNotInvitedRegistrationCheck;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreTeamInvitationRepositoryTest {
    @Test
    void testOperations(EclipseStoreTeamInvitationRepository teamInvitationRepository,
                        UserNotInvitedRegistrationCheck userNotInvitedRegistrationCheck) {
        final String email = "invitation@email.com";
        Tenant tenant = null;
        assertThat(teamInvitationRepository.findAll(null))
                .isEmpty();
        assertThat(userNotInvitedRegistrationCheck.validate(email, tenant))
                .isNotEmpty();
        assertThatCode(() -> teamInvitationRepository.save(new TeamInvitationRecord(email, null)))
                .doesNotThrowAnyException();
        assertThat(userNotInvitedRegistrationCheck.validate(email, tenant))
                .isEmpty();
        assertThat(teamInvitationRepository.findAll(null))
                .hasSize(1);
        assertThat(teamInvitationRepository.findAll(null).get(0))
                .hasFieldOrPropertyWithValue("email", email);
        assertThatThrownBy(() -> teamInvitationRepository.save(new TeamInvitationRecord(email, null)))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
