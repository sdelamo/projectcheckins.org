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

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Introspected
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RepeatPasswordForm> {
    @Override
    public boolean isValid(RepeatPasswordForm value, ConstraintValidatorContext context) {
        if (value.password() == null) {
            return value.repeatPassword() == null;
        }
        return value.password().equals(value.repeatPassword());
    }
}
