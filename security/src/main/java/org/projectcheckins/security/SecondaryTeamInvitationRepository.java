package org.projectcheckins.security;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Secondary
@Singleton
public class SecondaryTeamInvitationRepository implements TeamInvitationRepository {
    @Override
    public List<? extends TeamInvitation> findAll(@Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void save(@NonNull @NotNull @Valid TeamInvitation invitation) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteByEmail(@NotBlank @Email String email, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean existsByEmail(@NotBlank @Email String email, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
