package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.UserFetcher;
import org.projectcheckins.security.UserState;

import java.util.Optional;

@Singleton
class EclipseStoreUserFetcher implements UserFetcher {
    private final RootProvider<Data> rootProvider;

    EclipseStoreUserFetcher(RootProvider<Data> rootProvider) {
        this.rootProvider = rootProvider;
    }

    @Override
    @NonNull
    public Optional<UserState> findByEmail(@NotBlank @NonNull String email) {
        return rootProvider.root().getUsers().stream()
                .filter(user -> user.email().equals(email))
                .map(EclipseStoreUserFetcher::userStateOfEntity)
                .findFirst();
    }

    @NonNull
    private static UserState userStateOfEntity(UserEntity user) {
        return new UserState() {
            @Override
            public String getId() {
                return user.id();
            }

            @Override
            public String getEmail() {
                return user.email();
            }

            @Override
            public String getPassword() {
                return user.encodedPassword();
            }
        };
    }
}
