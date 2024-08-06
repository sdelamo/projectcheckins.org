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

package org.projectcheckins.security.http;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class SignUpFormTest {

    @Test
    void emailCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new SignUpForm("", "secret", "secret")).isEmpty());
        assertFalse(validator.validate(new SignUpForm("", "secret", "secret")).isEmpty());
    }

    @Test
    void passwordAndRepeatPasswordCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "", "")).isEmpty());
        assertFalse(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "", "")).isEmpty());
    }

    @Test
    void passwordAndRepeatPasswordShouldMatch(Validator validator) {
        assertFalse(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "secret", "foobar")).isEmpty());
    }

    @Test
    void matchingPasswordsAndEmailisValid(Validator validator) {
        assertTrue(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "secret", "secret")).isEmpty());
    }

    @Test
    void signupFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(SignUpForm.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void signupFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(SignUpForm.class)))
                .doesNotThrowAnyException();
    }
}