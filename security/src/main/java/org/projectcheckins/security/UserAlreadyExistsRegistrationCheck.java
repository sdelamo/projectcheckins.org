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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.views.fields.messages.Message;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;

@Singleton
public class UserAlreadyExistsRegistrationCheck implements RegistrationCheck {
    private static final Message MESSAGE_USER_ALREADY_EXISTS = new Message("User already exists", "user.already.exists");
    public static final RegistrationCheckViolation VIOLATION_USER_ALREADY_EXISTS = new RegistrationCheckViolation(MESSAGE_USER_ALREADY_EXISTS);
    private final UserRepository userRepository;

    public UserAlreadyExistsRegistrationCheck(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NonNull
    public Optional<RegistrationCheckViolation> validate(@NotBlank @Email String email, @Nullable Tenant tenant) {
        return userRepository.existsByEmail(email, tenant)
                ? Optional.of(VIOLATION_USER_ALREADY_EXISTS)
                : Optional.empty();
    }
}
