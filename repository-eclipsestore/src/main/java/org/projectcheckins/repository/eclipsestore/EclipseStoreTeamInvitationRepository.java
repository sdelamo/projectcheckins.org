// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.TeamInvitationRepository;

import java.util.List;
import java.util.Optional;

@Singleton
class EclipseStoreTeamInvitationRepository implements TeamInvitationRepository {
    private final RootProvider<Data> rootProvider;

    protected EclipseStoreTeamInvitationRepository(RootProvider<Data> rootProvider) {
        this.rootProvider = rootProvider;
    }

    @Override
    public List<TeamInvitationEntity> findAll(@Nullable Tenant tenant) {
        return rootProvider.root().getInvitations();
    }

    @Override
    public void save(@NonNull @NotNull @Valid TeamInvitation invitation) {
        String email = invitation.email();
        if (findByEmail(email).isEmpty()) {
            saveInvitation(rootProvider.root().getInvitations(), new TeamInvitationEntity(email));
        }
    }

    @Override
    public void deleteByEmail(@NotBlank @Email String email, @Nullable Tenant tenant) {
        findByEmail(email).ifPresent(e -> delete(rootProvider.root().getInvitations(), e));
    }

    @Override
    public boolean existsByEmail(@NotBlank @Email String email, @Nullable Tenant tenant) {
        return findByEmail(email).isPresent();
    }

    private Optional<TeamInvitationEntity> findByEmail(@NotBlank @Email String email) {
        return rootProvider.root().getInvitations().stream().filter(i -> i.email().equals(email)).findAny();
    }

    @StoreParams("invitations")
    public void saveInvitation(List<TeamInvitationEntity> invitations, TeamInvitationEntity invitation) {
        invitations.add(invitation);
    }

    @StoreParams("invitations")
    public void delete(List<TeamInvitationEntity> invitations, TeamInvitationEntity invitation) {
        invitations.remove(invitation);
    }
}
