package org.projectcheckins.security;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Secondary
@Singleton
public class SecondaryUserRepository implements UserRepository {
    @Override
    public boolean existsByEmail(@NotBlank @Email String email, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteByEmail(@NotBlank @Email String email, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void updateAuthorities(@NotBlank @Email String email,
                                  @NonNull List<String> authorities,
                                  @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
