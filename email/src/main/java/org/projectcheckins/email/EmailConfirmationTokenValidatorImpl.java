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

import com.nimbusds.jwt.JWT;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.validator.JsonWebTokenValidator;
import io.micronaut.security.token.jwt.validator.JwtAuthenticationFactory;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Singleton
class EmailConfirmationTokenValidatorImpl implements EmailConfirmationTokenValidator {
    private static final Logger LOG = LoggerFactory.getLogger(EmailConfirmationTokenValidatorImpl.class);
    private final JsonWebTokenValidator<JWT, ?> tokenValidator;
    private final JwtAuthenticationFactory jwtAuthenticationFactory;

    EmailConfirmationTokenValidatorImpl(JsonWebTokenValidator<JWT, ?> tokenValidator,
                                        JwtAuthenticationFactory jwtAuthenticationFactory) {
        this.tokenValidator = tokenValidator;
        this.jwtAuthenticationFactory = jwtAuthenticationFactory;
    }

    @Override
    @NonNull
    public Optional<Authentication> validate(@NonNull @NotBlank String token) {
        return tokenValidator.validate(token, null)
                .flatMap(jwtAuthenticationFactory::createAuthentication);
    }
}
