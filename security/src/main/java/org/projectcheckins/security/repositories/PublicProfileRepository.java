package org.projectcheckins.security.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import org.projectcheckins.security.api.PublicProfile;

import java.util.List;

public interface PublicProfileRepository {

  @NonNull
  List<? extends PublicProfile> list(@Nullable Tenant tenant);

  @NonNull
  default List<? extends PublicProfile> list() {
      return list(null);
  }
}
