package org.projectcheckins.security.constraints;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.*;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "UserDoesNotExistValidatorTest")
class UserDoesNotExistValidatorTest {
    private static final String EXISTING_EMAIL = "calvog@unityfoundation.io";
    @Test
    void isValid(UserDoesNotExistValidator userDoesNotExistValidator) {
        assertThat(userDoesNotExistValidator.isValid(new TeamInvitationRecord(null, null), null, null)).isTrue();
        assertThat(userDoesNotExistValidator.isValid(new TeamInvitationRecord("", null), null, null)).isTrue();
        assertThat(userDoesNotExistValidator.isValid(new TeamInvitationRecord("delamos@unityfoundation.io", null), null, null)).isTrue();
        assertThat(userDoesNotExistValidator.isValid(new TeamInvitationRecord(EXISTING_EMAIL, null), null, null)).isFalse();
    }

    @Requires(property = "spec.name", value = "UserDoesNotExistValidatorTest")
    @Singleton
    @Replaces(UserAlreadyExistsRegistrationCheck.class)
    static class UserAlreadyExistsRegistrationCheckMock extends UserAlreadyExistsRegistrationCheck {

        public UserAlreadyExistsRegistrationCheckMock(UserRepository userRepository) {
            super(userRepository);
        }

        @Override
        public Optional<RegistrationCheckViolation> validate(String email, Tenant tenant) {
            if (email.equals(EXISTING_EMAIL)) {
                return Optional.of(VIOLATION_USER_ALREADY_EXISTS);
            }
            return Optional.empty();
        }
    }
}