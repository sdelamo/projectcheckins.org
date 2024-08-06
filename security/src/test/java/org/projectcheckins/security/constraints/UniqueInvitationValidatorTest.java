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

package org.projectcheckins.security.constraints;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.SecondaryTeamInvitationRepository;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.TeamInvitationRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "UniqueValidatorTest")
class UniqueInvitationValidatorTest {
    private static final String EXISTING_EMAIL = "calvog@unityfoundation.io";
    @Test
    void isValid(UniqueValidator uniqueValidator) {
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord(null, null), null, null)).isTrue();
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord("", null), null, null)).isTrue();
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord("delamos@unityfoundation.io", null), null, null)).isTrue();
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord(EXISTING_EMAIL, null), null, null)).isFalse();
    }

    @Requires(property = "spec.name", value = "UniqueValidatorTest")
    @Singleton
    @Replaces(TeamInvitationRepository.class)
    static class TeamInvitationRepositoryMock extends SecondaryTeamInvitationRepository {

        @Override
        public boolean existsByEmail(String email, Tenant tenant) {
            if (email.equals(EXISTING_EMAIL)) {
                return true;
            }
            return false;
        }
    }
}