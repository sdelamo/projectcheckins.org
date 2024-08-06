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

package org.projectcheckins.email;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.generator.TokenGenerator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Property(name = "spec.name", value = "EmailGeneratorConfigurationExceptionTest")
@MicronautTest(startApplication = false)
class EmailGeneratorConfigurationExceptionTest {

    @Test
    void configurationEx(EmailConfirmationTokenGenerator generator) {
        assertThatThrownBy(() -> generator.generateToken("sergio.delamo@softamo.com"))
            .isInstanceOf(ConfigurationException.class);
    }

    @Singleton
    @Requires(property = "spec.name", value = "EmailGeneratorConfigurationExceptionTest")
    @Replaces(TokenGenerator.class)
    static class TokenGeneratorMock implements TokenGenerator {

        @Override
        public Optional<String> generateToken(Authentication authentication, @Nullable Integer expiration) {
            return Optional.empty();
        }

        @Override
        public Optional<String> generateToken(Map<String, Object> claims) {
            return Optional.empty();
        }
    }
}
