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
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret", value="pleaseChangeThisSecretForANewOne")
@MicronautTest
class EmailConfirmationTokenGeneratorTest {

    @Test
    void testTokenGenerationAndValidation(EmailConfirmationTokenGenerator emailConfirmationTokenGenerator,
                                          EmailConfirmationTokenValidator emailConfirmationTokenValidator) {
        String email = "delamos@unityfoundation.io";
        String token = emailConfirmationTokenGenerator.generateToken(email);
        assertThat(token).isNotNull();
        assertThat(emailConfirmationTokenValidator.validate(token))
            .hasValueSatisfying(a -> assertThat(a).hasFieldOrPropertyWithValue("name", email));
    }
}