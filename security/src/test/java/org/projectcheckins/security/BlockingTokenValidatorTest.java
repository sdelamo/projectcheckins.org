package org.projectcheckins.security;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.security.token.jwt.validator.JwtTokenValidator;
import io.micronaut.security.token.validator.TokenValidator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "BlockingTokenValidatorTest")
class BlockingTokenValidatorTest {

    static final Authentication AUTH = new ClientAuthentication("ok", null);

    @Inject
    BlockingTokenValidator blockingTokenValidator;

    @Test
    void testValidToken() {
        assertThat(blockingTokenValidator.validateToken("valid")).contains(AUTH);
    }

    @Test
    void testInvalidToken() {
        assertThat(blockingTokenValidator.validateToken("invalid")).isEmpty();
    }

    @Test
    void testErrorToken() {
        assertThat(blockingTokenValidator.validateToken("error")).isEmpty();
    }

    @Requires(property = "spec.name", value = "BlockingTokenValidatorTest")
    @Singleton
    @Replaces(JwtTokenValidator.class)
    static class TokenValidatorMock<T> implements TokenValidator<T> {
        @Override
        public Publisher<Authentication> validateToken(String token, @Nullable T request) {
            return switch (token) {
                case "valid" -> Mono.just(AUTH);
                case "error" -> Mono.error(new RuntimeException("Whoops"));
                default -> Mono.empty();
            };
        }
    }
}
