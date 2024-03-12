package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.repositories.ProfileRepository;

@Singleton
class EclipseStoreProfileRepository implements ProfileRepository {
  private final RootProvider<Data> rootProvider;

  public EclipseStoreProfileRepository(RootProvider<Data> rootProvider) {
    this.rootProvider = rootProvider;
  }

  @Override
  @NonNull
  public Optional<Profile> findByAuthentication(@NotNull Authentication authentication, @Nullable Tenant tenant) {
    return findFirst(authentication).map(this::fromEntity);
  }

  @Override
  public void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant) {
    final UserEntity entity = findFirst(authentication).orElseThrow(UserNotFoundException::new);
    save(updateEntity(entity, profileUpdate));
  }

  @StoreParams("user")
  public void save(UserEntity user) {
  }

  private Optional<UserEntity> findFirst(Authentication authentication) {
    final String name = authentication.getName();
    return rootProvider.root().getUsers().stream().filter(u -> u.getId().equals(name)).findFirst();
  }

  private UserEntity updateEntity(UserEntity entity, ProfileUpdate profileUpdate) {
    entity.setTimeZone(profileUpdate.timeZone());
    entity.setFirstDayOfWeek(profileUpdate.firstDayOfWeek());
    entity.setTimeFormat(profileUpdate.timeFormat());
    entity.setFirstName(profileUpdate.firstName());
    entity.setLastName(profileUpdate.lastName());
    return entity;
  }

  private Profile fromEntity(UserEntity entity) {
    return new Profile(
            entity.getEmail(),
        entity.getTimeZone(),
        entity.getFirstDayOfWeek(),
        entity.getTimeFormat(),
        entity.getFirstName(),
        entity.getLastName()
    );
  }
}
