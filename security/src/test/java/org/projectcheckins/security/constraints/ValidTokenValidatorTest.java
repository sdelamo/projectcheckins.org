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

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.BlockingTokenValidator;
import org.projectcheckins.security.BlockingTokenValidatorImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "ValidTokenValidatorTest")
class ValidTokenValidatorTest {

    @Inject
    ValidTokenValidator validTokenValidator;

    @Test
    void testValidToken() {
        assertThat(validTokenValidator.isValid("valid", null))
                .isTrue();
    }

    @Test
    void testInvalidToken() {
        assertThat(validTokenValidator.isValid("invalid", null))
                .isFalse();
    }

    @Requires(property = "spec.name", value = "ValidTokenValidatorTest")
    @Singleton
    @Replaces(BlockingTokenValidatorImpl.class)
    static class BlockingTokenValidatorMock implements BlockingTokenValidator {
        @Override
        public Optional<Authentication> validateToken(String token) {
            return switch (token) {
                case "valid" -> Optional.of(new ClientAuthentication("ok", null));
                default -> Optional.empty();
            };
        }
    }
}
