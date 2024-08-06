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

package org.projectcheckins.test;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Set;

public class ValidationAssert<T> extends AbstractAssert<ValidationAssert<T>, Set<ConstraintViolation<T>>> {

    public ValidationAssert(Set<ConstraintViolation<T>> violations) {
        super(violations, ValidationAssert.class);
    }

    public static <T> ValidationAssert<T> assertThat(Set<ConstraintViolation<T>> violations) {
        return new ValidationAssert<>(violations);
    }

    public ValidationAssert <T> fieldNotBlank(String name) {
        expectedViolationMessage(name, "must not be blank");
        return this;
    }

    public ValidationAssert<T> isValid() {
        Assertions.assertThat(actual).isEmpty();
        return this;
    }

    public ValidationAssert<T> hasNotNullViolation(String name) {
        expectedViolationMessage(name, "must not be null");
        return this;
    }

    public ValidationAssert<T> hasNotBlankViolation(String name) {
        expectedViolationMessage(name, "must not be blank");
        return this;
    }

    public ValidationAssert<T> hasMalformedEmailViolation(String name) {
        expectedViolationMessage(name, "must be a well-formed email address");
        return this;
    }

    public ValidationAssert<T> hasNotEmptyViolation(String name) {
        expectedViolationMessage(name, "must not be empty");
        return this;
    }

    private void expectedViolationMessage(String name, String message) {
        Assertions.assertThat(actual)
                .anyMatch(x -> x.getPropertyPath().toString().equals(name))
                .extracting(ConstraintViolation::getMessage)
                .anyMatch(message::equals);
    }


    public ValidationAssert<T> hasErrorMessage(String message) {
        Assertions.assertThat(actual)
                .extracting(ConstraintViolation::getMessage)
                .anyMatch(message::equals);
        return this;
    }
}
