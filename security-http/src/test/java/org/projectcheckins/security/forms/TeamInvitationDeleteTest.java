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

package org.projectcheckins.security.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class TeamInvitationDeleteTest {

    final static Argument<TeamInvitationDelete> ARGUMENT = Argument.of(TeamInvitationDelete.class);

    @Test
    void emailIsRequired(Validator validator) {
        assertThat(validator.validate(new TeamInvitationDelete(null)))
                .hasNotBlankViolation("email");
        assertThat(validator.validate(new TeamInvitationDelete("")))
                .hasNotBlankViolation("email");
        assertThat(validator.validate(new TeamInvitationDelete("example@projectcheckins.org")))
                .isValid();
    }

    @Test
    void teamMemberDeleteIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serde) {
        assertThatCode(() -> serde.getDeserializableIntrospection(ARGUMENT))
                .doesNotThrowAnyException();
    }

    @Test
    void teamMemberDeleteIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serde) {
        assertThatCode(() -> serde.getSerializableIntrospection(ARGUMENT))
                .doesNotThrowAnyException();
    }
}
