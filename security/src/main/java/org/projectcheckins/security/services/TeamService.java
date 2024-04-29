package org.projectcheckins.security.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.api.PublicProfile;
import org.projectcheckins.security.forms.TeamMemberDelete;
import org.projectcheckins.security.forms.TeamMemberSave;
import org.projectcheckins.security.forms.TeamInvitationDelete;
import org.projectcheckins.security.TeamInvitation;

import java.util.List;
import java.util.Locale;

public interface TeamService {

    @NonNull
    List<? extends PublicProfile> findAll(@Nullable Tenant tenant);

    @NonNull
    List<? extends TeamInvitation> findInvitations(@Nullable Tenant tenant);

    void save(@NotNull @Valid TeamMemberSave form, @Nullable Tenant tenant, @NotNull Locale locale, @NotBlank String signupUrl);

    void remove(@NotNull @Valid TeamMemberDelete form, @Nullable Tenant tenant);

    void uninvite(@NotNull @Valid TeamInvitationDelete form, @Nullable Tenant tenant);
}
