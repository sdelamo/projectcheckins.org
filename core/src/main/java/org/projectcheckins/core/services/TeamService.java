package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.security.TeamInvitation;

import java.util.List;

public interface TeamService {

    @NonNull
    List<? extends PublicProfile> findAll(@Nullable Tenant tenant);

    @NonNull
    List<? extends TeamInvitation> findInvitations(@Nullable Tenant tenant);

    void save(@NotNull @Valid TeamMemberSave form, @Nullable Tenant tenant);
}
