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
import io.micronaut.http.HttpRequest;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static io.micronaut.scheduling.TaskExecutors.BLOCKING;

@Singleton
public class BlockingTokenValidatorImpl implements BlockingTokenValidator {

    private static final Logger LOG = LoggerFactory.getLogger(BlockingTokenValidatorImpl.class);

    private final TokenValidator<HttpRequest<?>> tokenValidator;
    private final ExecutorService executor;

    public BlockingTokenValidatorImpl(TokenValidator<HttpRequest<?>> tokenValidator, @Named(BLOCKING) ExecutorService executor) {
        this.tokenValidator = tokenValidator;
        this.executor = executor;
    }

    @Override
    @NonNull
    public Optional<Authentication> validateToken(@NonNull @NotBlank String token) {
        try {
            final Publisher<Authentication> result = tokenValidator.validateToken(token, ServerRequestContext.currentRequest().orElse(null));
            final Callable<Optional<Authentication>> task = Mono.from(result)::blockOptional;
            return executor.submit(task).get();
        } catch (ExecutionException | InterruptedException e) {
            LOG.warn("Could not validate token {}", token, e);
            return Optional.empty();
        }
    }
}
