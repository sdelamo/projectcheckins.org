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

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.projectcheckins.security.BlockingTokenValidator;

import java.util.Optional;

@Singleton
public class ValidTokenValidator implements ConstraintValidator<ValidToken, String> {

    private final BlockingTokenValidator blockingTokenValidator;

    public ValidTokenValidator(BlockingTokenValidator blockingTokenValidator) {
        this.blockingTokenValidator = blockingTokenValidator;
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {
        return Optional.ofNullable(token).flatMap(blockingTokenValidator::validateToken).isPresent();
    }
}
