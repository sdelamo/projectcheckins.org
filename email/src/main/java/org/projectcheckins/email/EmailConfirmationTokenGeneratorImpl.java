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
