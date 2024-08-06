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

package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.security.repositories.PublicProfileRepository;

public interface ProfileRepository extends PublicProfileRepository {

  @NonNull
  Optional<? extends Profile> findById(@NotBlank String id, @Nullable Tenant tenant);

  @NonNull
  default Optional<? extends Profile> findByAuthentication(@NotNull Authentication authentication, @Nullable Tenant tenant) {
    return findById(authentication.getName(), tenant);
  }

  void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant);

  @NonNull
  default Optional<? extends Profile> findByAuthentication(@NotNull Authentication authentication) {
    return findByAuthentication(authentication, null);
  }

  default void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate) {
    update(authentication, profileUpdate, null);
  }
}
