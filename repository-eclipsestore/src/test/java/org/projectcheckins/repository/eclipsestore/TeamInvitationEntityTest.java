package org.projectcheckins.repository.eclipsestore;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeamInvitationEntityTest {

    @Test
    void settersAndGetters() {
        TeamInvitationEntity invitation = new TeamInvitationEntity("email@projectcheckins.org");

        assertThat(invitation)
                .hasFieldOrPropertyWithValue("email", "email@projectcheckins.org");
        invitation.email("newEmail@projectcheckins.org");

        assertThat(invitation)
                .hasFieldOrPropertyWithValue("email", "newEmail@projectcheckins.org");
    }

}
