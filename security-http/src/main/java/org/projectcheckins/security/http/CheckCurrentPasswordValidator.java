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

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectcheckins.security.PasswordEncoder;
import org.projectcheckins.security.UserFetcher;

@Singleton
class CheckCurrentPasswordValidator implements ConstraintValidator<CheckCurrentPassword, PasswordForm> {

    private final UserFetcher userFetcher;
    private final PasswordEncoder passwordEncoder;

    CheckCurrentPasswordValidator(UserFetcher userFetcher, PasswordEncoder passwordEncoder) {
        this.userFetcher = userFetcher;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isValid(PasswordForm value, ConstraintValidatorContext context) {
        return userFetcher.findById(value.userId())
                .map(u -> passwordEncoder.matches(value.currentPassword(), u.getPassword()))
                .orElse(false);
    }
}
