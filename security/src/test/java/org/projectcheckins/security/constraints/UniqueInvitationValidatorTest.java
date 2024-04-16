package org.projectcheckins.security.constraints;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.SecondaryTeamInvitationRepository;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.TeamInvitationRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "UniqueValidatorTest")
class UniqueInvitationValidatorTest {
    private static final String EXISTING_EMAIL = "calvog@unityfoundation.io";
    @Test
    void isValid(UniqueValidator uniqueValidator) {
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord(null, null), null, null)).isTrue();
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord("", null), null, null)).isTrue();
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord("delamos@unityfoundation.io", null), null, null)).isTrue();
        assertThat(uniqueValidator.isValid(new TeamInvitationRecord(EXISTING_EMAIL, null), null, null)).isFalse();
    }

    @Requires(property = "spec.name", value = "UniqueValidatorTest")
    @Singleton
    @Replaces(TeamInvitationRepository.class)
    static class TeamInvitationRepositoryMock extends SecondaryTeamInvitationRepository {

        @Override
        public boolean existsByEmail(String email, Tenant tenant) {
            if (email.equals(EXISTING_EMAIL)) {
                return true;
            }
            return false;
        }
    }
}