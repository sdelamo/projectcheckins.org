package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;

import java.util.List;


@Singleton
public class TeamServiceImpl implements TeamService {

    private final ProfileRepository profileRepository;
    private final RegisterService registerService;

    public TeamServiceImpl(ProfileRepository profileRepository, RegisterService registerService) {
        this.profileRepository = profileRepository;
        this.registerService = registerService;
    }

    @Override
    @NonNull
    public List<? extends PublicProfile> findAll(@Nullable Tenant tenant) {
        return profileRepository.list(tenant);
    }

    @Override
    @NotBlank
    public String save(@NotNull TeamMemberSave form, @Nullable Tenant tenant) throws UserAlreadyExistsException {
        return registerService.register(form.email(), "secret");
    }
}
