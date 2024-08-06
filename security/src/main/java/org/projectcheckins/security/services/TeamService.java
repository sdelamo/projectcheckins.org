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
import org.projectcheckins.security.forms.TeamMemberUpdate;

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

    void update(@NotNull @Valid TeamMemberUpdate form, @Nullable Tenant tenant);
}
