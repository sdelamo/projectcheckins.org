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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordMatchValidatorTest {
    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(PasswordMatchValidator.class));
    }

    @Test
    void passwordMatching() {
        PasswordMatchValidator validator = new PasswordMatchValidator();
        assertTrue(validator.isValid(new SignUpForm("admin", "admin123", "admin123"), null));
        assertFalse(validator.isValid(new SignUpForm("admin", "admin123", "foobar"), null));
        assertTrue(validator.isValid(new SignUpForm("admin", null, null), null));
        assertFalse(validator.isValid(new SignUpForm("admin", "admin123", null), null));
        assertFalse(validator.isValid(new SignUpForm("admin",  null, "admin123"), null));
    }

    @PasswordMatch
    @Serdeable
    public record SignUpForm(@NonNull @NotBlank @Email String email,
                             @NonNull @NotBlank String password,
                             @NonNull @NotBlank String repeatPassword) implements RepeatPasswordForm {

    }
}