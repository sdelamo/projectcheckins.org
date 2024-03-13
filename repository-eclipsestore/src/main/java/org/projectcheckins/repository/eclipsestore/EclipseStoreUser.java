package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
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
class EclipseStoreUser extends AbstractRegisterService implements UserFetcher, EmailConfirmationRepository {
    private final ProfileConfiguration profileConfiguration;
    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;
    protected EclipseStoreUser(PasswordEncoder passwordEncoder,
                               ProfileConfiguration profileConfiguration,
                               RootProvider<Data> rootProvider,
                               IdGenerator idGenerator) {
        super(passwordEncoder);
        this.profileConfiguration = profileConfiguration;
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    public String register(@NonNull UserSave userSave) throws UserAlreadyExistsException {
        if (rootProvider.root().getUsers().stream().anyMatch(user -> user.getEmail().equals(userSave.email()))) {
            throw new UserAlreadyExistsException();
        }
        String id = idGenerator.generate();
        UserEntity userEntity = entityOf(userSave);
        userEntity.setId(id);
        saveUser(rootProvider.root().getUsers(), userEntity);
        return id;
    }

    @Override
    public void enableByEmail(@NonNull @NotBlank @Email String email) {
        rootProvider.root().getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .ifPresent(this::enableUser);
    }

    @StoreParams("users")
    public void saveUser(List<UserEntity> users, UserEntity userEntity) {
        users.add(userEntity);
    }

    @StoreParams("user")
    public void enableUser(UserEntity user) {
        user.setEnabled(true);
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
    public Optional<UserState> findByEmail(@NotBlank @NonNull String email) {
        return rootProvider.root().getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .map(EclipseStoreUser::userStateOfEntity)
                .findFirst();
    }

    @NonNull
    private static UserState userStateOfEntity(UserEntity user) {
        return new UserState() {
            @Override
            public String getId() {
                return user.getId();
            }

            @Override
            public boolean isEnabled() {
                return user.isEnabled();
            }

            @Override
            public String getEmail() {
                return user.getEmail();
            }

            @Override
            public String getPassword() {
                return user.getEncodedPassword();
            }
        };
    }
}
