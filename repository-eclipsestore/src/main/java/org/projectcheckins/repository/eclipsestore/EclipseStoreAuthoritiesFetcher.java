package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.AuthoritiesFetcher;

import java.util.Collections;
import java.util.List;

@Singleton
class EclipseStoreAuthoritiesFetcher implements AuthoritiesFetcher {
    private final RootProvider<Data> rootProvider;

    EclipseStoreAuthoritiesFetcher(RootProvider<Data> rootProvider) {
        this.rootProvider = rootProvider;
    }

    @Override
    @NonNull
    public List<String> findAuthoritiesByEmail(@NotBlank String email) {
        return rootProvider.root().getUsers()
                .stream()
                .filter(user -> user.email().equals(email))
                .map(UserEntity::authorities)
                .findFirst()
                .orElseGet(Collections::emptyList);
    }
}
