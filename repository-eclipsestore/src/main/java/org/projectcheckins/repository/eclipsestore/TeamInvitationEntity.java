package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.TeamInvitation;

@Introspected
public class TeamInvitationEntity implements TeamInvitation {

    @NotBlank
    private String email;



    public TeamInvitationEntity(String email) {
        this.email = email;
    }

    @Override
    @Nullable
    public Tenant tenant() {
        return null;
    }

    public String email() {
        return email;
    }

    public void email(String email) {
        this.email = email;
    }
}
