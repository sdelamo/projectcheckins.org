package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.security.*;

import java.util.Collections;

@Singleton
class EclipseStoreRegisterService extends AbstractRegisterService {

    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;
    protected EclipseStoreRegisterService(PasswordEncoder passwordEncoder,
                                          RootProvider<Data> rootProvider,
                                          IdGenerator idGenerator) {
        super(passwordEncoder);
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    public String register(@NonNull UserSave userSave) throws UserAlreadyExistsException {
        if (rootProvider.root().getUsers().stream().anyMatch(user -> user.email().equals(userSave.email()))) {
            throw new UserAlreadyExistsException();
        }
        String id = idGenerator.generate();
        rootProvider.root().getUsers().add(new UserEntity(id, userSave.email(), userSave.encodedPassword(), userSave.authorities()));
        return id;
    }
}
