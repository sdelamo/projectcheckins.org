package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.ProfileUpdate;

public interface ProfileRepository {

  @NonNull
  List<? extends PublicProfile> list(@Nullable Tenant tenant);

  @NonNull
  Optional<? extends Profile> findById(@NotBlank String id, @Nullable Tenant tenant);

  @NonNull
  default Optional<? extends Profile> findByAuthentication(@NotNull Authentication authentication, @Nullable Tenant tenant) {
    return findById(authentication.getName(), tenant);
  }

  void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant);

  @NonNull
  default List<? extends PublicProfile> list() {
      return list(null);
  }

  @NonNull
  default Optional<? extends Profile> findByAuthentication(@NotNull Authentication authentication) {
    return findByAuthentication(authentication, null);
  }

  default void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate) {
    update(authentication, profileUpdate, null);
  }
}
