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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class LoginFormTest {

    public static final String EMAIL = "manolo@projectcheckins.org";

    @Test
    void usernameIsRequired(Validator validator) {
        assertThat(validator.validate(new LoginForm(null, "rawpassword")))
            .isNotEmpty();
        assertThat(validator.validate(new LoginForm("", "rawpassword")))
                .anyMatch(x -> x.getPropertyPath().toString().equals("username"))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must not be blank");

        assertThat(validator.validate(new LoginForm("foo", "rawpassword")))
                .anyMatch(x -> x.getPropertyPath().toString().equals("username"))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must be a well-formed email address");
        assertThat(validator.validate(new LoginForm(EMAIL, "rawpassword")))
            .isEmpty();
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertThat(validator.validate(new LoginForm(EMAIL, null)))
            .isNotEmpty();
        assertThat(validator.validate(new LoginForm(EMAIL, "")))
            .anyMatch(x -> x.getPropertyPath().toString().equals("password") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void loginFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(LoginForm.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void loginFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(LoginForm.class)))
            .doesNotThrowAnyException();
    }

}