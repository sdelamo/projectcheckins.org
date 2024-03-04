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