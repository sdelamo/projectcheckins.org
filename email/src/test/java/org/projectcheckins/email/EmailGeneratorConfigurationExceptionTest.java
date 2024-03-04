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
