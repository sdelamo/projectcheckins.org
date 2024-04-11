package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.security.UserAlreadyExistsException;

import java.util.List;

public interface TeamService {

    @NonNull
    List<? extends PublicProfile> findAll(@Nullable Tenant tenant);

    @NotBlank
    String save(@NotNull @Valid TeamMemberSave form, @Nullable Tenant tenant) throws UserAlreadyExistsException;
}
