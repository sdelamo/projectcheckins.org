package org.projectcheckins.security.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.api.PublicProfile;
import org.projectcheckins.security.forms.TeamMemberSave;
import org.projectcheckins.security.repositories.PublicProfileRepository;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.TeamInvitationRecord;
import org.projectcheckins.security.TeamInvitationRepository;

import java.util.List;

@Singleton
public class TeamServiceImpl implements TeamService {

    private final PublicProfileRepository profileRepository;
    private final TeamInvitationRepository teamInvitationRepository;

    public TeamServiceImpl(PublicProfileRepository profileRepository, TeamInvitationRepository teamInvitationRepository) {
        this.profileRepository = profileRepository;
        this.teamInvitationRepository = teamInvitationRepository;
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
    public void save(@NotNull TeamMemberSave form, @Nullable Tenant tenant) {
        teamInvitationRepository.save(new TeamInvitationRecord(form.email(), tenant));
    }
}
