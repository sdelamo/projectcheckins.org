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

package org.projectcheckins.security.services;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.InvitationSavedEvent;
import org.projectcheckins.security.api.PublicProfile;
import org.projectcheckins.security.forms.TeamMemberDelete;
import org.projectcheckins.security.forms.TeamMemberSave;
import org.projectcheckins.security.forms.TeamInvitationDelete;
import org.projectcheckins.security.forms.TeamMemberUpdate;
import org.projectcheckins.security.Role;
import org.projectcheckins.security.repositories.PublicProfileRepository;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.security.UserRepository;

import java.util.List;
import java.util.Locale;

@Singleton
public class TeamServiceImpl implements TeamService {

    private static final List<String> ADMIN = List.of(Role.ROLE_ADMIN);
    private static final List<String> MEMBER = List.of();

    private final PublicProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final ApplicationEventPublisher<InvitationSavedEvent> invitationSavedEventPublisher;

    public TeamServiceImpl(PublicProfileRepository profileRepository,
                           UserRepository userRepository,
                           TeamInvitationRepository teamInvitationRepository,
                           ApplicationEventPublisher<InvitationSavedEvent> invitationSavedEventPublisher) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.teamInvitationRepository = teamInvitationRepository;
        this.invitationSavedEventPublisher = invitationSavedEventPublisher;
    }

    @Override
    @NonNull
    public List<? extends PublicProfile> findAll(@Nullable Tenant tenant) {
        return profileRepository.list(tenant);
    }

    @Override
    @NonNull
    public List<? extends TeamInvitation> findInvitations(@Nullable Tenant tenant) {
        return teamInvitationRepository.findAll(tenant);
    }

    @Override
    public void save(@NotNull TeamMemberSave form, @Nullable Tenant tenant, @NotNull Locale locale, @NotBlank String signupUrl) {
        teamInvitationRepository.save(new TeamInvitationRecord(form.email(), tenant));
        invitationSavedEventPublisher.publishEventAsync(new InvitationSavedEvent(form, locale, signupUrl));
    }

    @Override
    public void remove(@NotNull @Valid TeamMemberDelete form, @Nullable Tenant tenant) {
        userRepository.deleteByEmail(form.email(), tenant);
    }

    @Override
    public void uninvite(@NotNull TeamInvitationDelete form, @Nullable Tenant tenant) {
        teamInvitationRepository.deleteByEmail(form.email(), tenant);
    }

    @Override
    public void update(@NotNull @Valid TeamMemberUpdate form, @Nullable Tenant tenant) {
        userRepository.updateAuthorities(form.email(), form.isAdmin() ? ADMIN : MEMBER, tenant);
    }
}
