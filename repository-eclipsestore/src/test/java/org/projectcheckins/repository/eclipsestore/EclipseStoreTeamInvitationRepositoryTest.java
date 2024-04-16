package org.projectcheckins.repository.eclipsestore;

import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.UserNotInvitedRegistrationCheck;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreTeamInvitationRepositoryTest {
    @Test
    void testOperations(EclipseStoreTeamInvitationRepository teamInvitationRepository,
                        UserNotInvitedRegistrationCheck userNotInvitedRegistrationCheck) {
        final String email = "invitation@email.com";
        Tenant tenant = null;
        assertThat(teamInvitationRepository.findAll(null))
                .isEmpty();
        assertThat(userNotInvitedRegistrationCheck.validate(email, tenant))
                .isNotEmpty();
        assertThatCode(() -> teamInvitationRepository.save(new TeamInvitationRecord(email, null)))
                .doesNotThrowAnyException();
        assertThat(userNotInvitedRegistrationCheck.validate(email, tenant))
                .isEmpty();
        assertThat(teamInvitationRepository.findAll(null))
                .hasSize(1);
        assertThat(teamInvitationRepository.findAll(null).get(0))
                .hasFieldOrPropertyWithValue("email", email);
        assertThatThrownBy(() -> teamInvitationRepository.save(new TeamInvitationRecord(email, null)))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
