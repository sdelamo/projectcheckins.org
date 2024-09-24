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

package org.projectcheckins.security;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest(startApplication = false)
class UserSaveTest {
    @Test
    void usernameIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave(null, "encodedpassword", emptyList())))
            .isNotEmpty();
        assertThat(validator.validate(new UserSave("", "encodedpassword", emptyList())))
            .anyMatch(x -> x.getPropertyPath().toString().equals("email") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new UserSave("manolo", "encodedpassword", emptyList())))
            .isEmpty();
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave("manolo", null, emptyList())))
            .isNotEmpty();
        assertThat(validator.validate(new UserSave("manolo", "", emptyList())))
            .anyMatch(x -> x.getPropertyPath().toString().equals("encodedPassword") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(UserSave.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(UserSave.class)))
            .doesNotThrowAnyException();
    }

}