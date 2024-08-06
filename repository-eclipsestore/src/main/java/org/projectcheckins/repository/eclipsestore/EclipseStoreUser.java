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

package org.projectcheckins.repository.eclipsestore;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.token.generator.AccessTokenConfiguration;
import io.micronaut.security.token.generator.TokenGenerator;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.core.configuration.ProfileConfiguration;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.*;
import java.util.List;
import java.util.Optional;

@Singleton
class EclipseStoreUser extends AbstractRegisterService implements EmailConfirmationRepository {
    private final ProfileConfiguration profileConfiguration;
    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;
    protected EclipseStoreUser(
            PasswordEncoder passwordEncoder,
            List<RegistrationCheck> registrationChecks,
            TeamInvitationRepository teamInvitationRepository,
            BlockingTokenValidator blockingTokenValidator,
            TokenGenerator tokenGenerator,
            AccessTokenConfiguration accessTokenConfiguration,
            ApplicationEventPublisher<PasswordResetEvent> passwordResetEventPublisher,
            ProfileConfiguration profileConfiguration,
            RootProvider<Data> rootProvider,
            IdGenerator idGenerator) {
        super(passwordEncoder, registrationChecks, teamInvitationRepository, blockingTokenValidator, tokenGenerator, accessTokenConfiguration, passwordResetEventPublisher);
        this.profileConfiguration = profileConfiguration;
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    public String register(@NonNull UserSave userSave, @Nullable Tenant tenant) throws RegistrationCheckViolationException {
        if (rootProvider.root().getUsers().stream().anyMatch(user -> user.email().equals(userSave.email()))) {
            throw new RegistrationCheckViolationException(UserAlreadyExistsRegistrationCheck.VIOLATION_USER_ALREADY_EXISTS);
        }
        String id = idGenerator.generate();
        UserEntity userEntity = entityOf(userSave);
        userEntity.id(id);
        saveUser(rootProvider.root().getUsers(), userEntity);
        return id;
    }

    @Override
    public void updatePassword(@NonNull PasswordUpdate passwordUpdate) {
        final String id = passwordUpdate.userId();
        rootProvider.root().getUsers().stream()
                .filter(u -> u.id().equals(id)).findAny()
                .ifPresent(u -> updatePassword(u, passwordUpdate.newEncodedPassword()));
    }

    @Override
    public void enableByEmail(@NonNull @NotBlank @Email String email) {
        rootProvider.root().getUsers().stream()
                .filter(user -> user.email().equals(email))
                .findFirst()
                .ifPresent(this::enableUser);
    }

    @StoreParams("users")
    public void saveUser(List<UserEntity> users, UserEntity userEntity) {
        users.add(userEntity);
    }

    @StoreParams("user")
    public void enableUser(UserEntity user) {
        user.enabled(true);
    }

    @StoreParams("user")
    public void updatePassword(UserEntity user, String encodedPassword) {
        user.encodedPassword(encodedPassword);
    }

    @NonNull
    private UserEntity entityOf(@NonNull UserSave userSave) {
        return new UserEntity(
            null,
                userSave.email(),
                userSave.encodedPassword(),
                userSave.authorities(),
                profileConfiguration.getTimeZone(),
                profileConfiguration.getFirstDayOfWeek(),
                profileConfiguration.getBeginningOfDay(),
                profileConfiguration.getEndOfDay(),
                profileConfiguration.getTimeFormat(),
                profileConfiguration.getFormat(),
                null,
                null
        );
    }

    @Override
    @NonNull
    public Optional<UserState> findById(@NotBlank @NonNull String id) {
        return rootProvider.root().getUsers().stream()
                .filter(user -> user.id().equals(id))
                .map(EclipseStoreUser::userStateOfEntity)
                .findFirst();
    }

    @Override
    @NonNull
    public Optional<UserState> findByEmail(@NotBlank @NonNull String email) {
        return rootProvider.root().getUsers().stream()
                .filter(user -> user.email().equals(email))
                .map(EclipseStoreUser::userStateOfEntity)
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
            public boolean isEnabled() {
                return user.enabled();
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
