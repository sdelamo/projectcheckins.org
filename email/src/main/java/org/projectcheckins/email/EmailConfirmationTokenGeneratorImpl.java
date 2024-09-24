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

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.generator.AccessTokenConfiguration;
import io.micronaut.security.token.generator.TokenGenerator;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Singleton
class EmailConfirmationTokenGeneratorImpl implements EmailConfirmationTokenGenerator {

    private final TokenGenerator tokenGenerator;
    private final AccessTokenConfiguration accessTokenConfiguration;

    EmailConfirmationTokenGeneratorImpl(TokenGenerator tokenGenerator, AccessTokenConfiguration accessTokenConfiguration) {
        this.tokenGenerator = tokenGenerator;
        this.accessTokenConfiguration = accessTokenConfiguration;
    }

    @Override
    @NonNull
    public String generateToken(@NonNull @NotBlank @Email String email) {
        return tokenGenerator.generateToken(Authentication.build(email), accessTokenConfiguration.getExpiration())
                .orElseThrow(() -> new ConfigurationException("Failed to generate token"));
    }
}
