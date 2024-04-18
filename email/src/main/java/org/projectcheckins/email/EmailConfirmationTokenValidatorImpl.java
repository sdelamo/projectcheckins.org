package org.projectcheckins.email;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.security.authentication.Authentication;
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
    private final TokenValidator<?> tokenValidator;
    private final ExecutorService blockingExecutor;

    EmailConfirmationTokenValidatorImpl(TokenValidator<?> tokenValidator,
                                        @Named(TaskExecutors.BLOCKING) ExecutorService blockingExecutor) {
        this.tokenValidator = tokenValidator;
        this.blockingExecutor = blockingExecutor;
    }

    @Override
    @NonNull
    public Optional<Authentication> validate(@NonNull @NotBlank String token) {
        try {
            return blockingExecutor.submit(() ->
                    Mono.from(tokenValidator.validateToken(token, null)).blockOptional()
            ).get();
        } catch (ExecutionException e) {
            LOG.warn("ExecutionException validating toke {}", token);
        } catch (InterruptedException e) {
            LOG.warn("InterruptedException validating toke {}", token);
        }
        return Optional.empty();
    }
}
