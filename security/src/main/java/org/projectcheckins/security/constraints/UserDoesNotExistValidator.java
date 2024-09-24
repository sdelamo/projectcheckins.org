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

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.UserAlreadyExistsRegistrationCheck;

@Singleton
class UserDoesNotExistValidator implements ConstraintValidator<UserDoesNotExist, TeamInvitation> {
    private final UserAlreadyExistsRegistrationCheck userAlreadyExistsRegistrationCheck;

    UserDoesNotExistValidator(UserAlreadyExistsRegistrationCheck userAlreadyExistsRegistrationCheck) {
        this.userAlreadyExistsRegistrationCheck = userAlreadyExistsRegistrationCheck;
    }

    @Override
    public boolean isValid(@Nullable TeamInvitation invitation,
                           @NonNull AnnotationValue<UserDoesNotExist> annotationMetadata,
                           @NonNull ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(invitation.email())) {
            return true;
        }
        return userAlreadyExistsRegistrationCheck.validate(invitation.email(), invitation.tenant()).isEmpty();
    }
}
