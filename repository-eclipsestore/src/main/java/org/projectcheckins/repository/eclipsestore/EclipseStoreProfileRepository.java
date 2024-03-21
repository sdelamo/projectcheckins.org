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
import java.util.List;
import java.util.Optional;

import org.projectcheckins.core.exceptions.UserNotFoundException;
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
  public List<UserEntity> list(@Nullable Tenant tenant) {
    return rootProvider.root().getUsers();
  }

  @Override
  @NonNull
  public Optional<UserEntity> findByAuthentication(@NotNull Authentication authentication, @Nullable Tenant tenant) {
    return findFirst(authentication);
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
    return rootProvider.root().getUsers().stream().filter(u -> u.id().equals(name)).findFirst();
  }

  private UserEntity updateEntity(UserEntity entity, ProfileUpdate profileUpdate) {
    entity.timeZone(profileUpdate.timeZone());
    entity.firstDayOfWeek(profileUpdate.firstDayOfWeek());
    entity.beginningOfDay(profileUpdate.beginningOfDay());
    entity.endOfDay(profileUpdate.endOfDay());
    entity.timeFormat(profileUpdate.timeFormat());
    entity.format(profileUpdate.format());
    entity.firstName(profileUpdate.firstName());
    entity.lastName(profileUpdate.lastName());
    return entity;
  }
}
