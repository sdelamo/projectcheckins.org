package org.projectcheckins.repository.eclipsestore;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserFetcher;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class EclipseStoreUserFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";


    @Test
    void authoritiesFetcher(UserFetcher userFetcher, RegisterService registerService) throws UserAlreadyExistsException {
        assertThat(userFetcher.findByEmail(NOT_FOUND_EMAIL))
            .isEmpty();
        registerService.register(FOUND_EMAIL, "password");
        assertThat(userFetcher.findByEmail(FOUND_EMAIL)).hasValueSatisfying(userState -> assertThat(userState)
                .matches(u -> !u.isEnabled())
                .matches(u -> u.getEmail().equals(FOUND_EMAIL))
                .matches(u -> u.getId() != null)
                .matches(u -> u.getPassword() != null && !u.getPassword().equals("password")));
    }

    @Test
    void testRegister(RegisterService registerService) {
        String email = "sergio@projectcheckins.org";
        assertThatCode(() -> registerService.register(email, "foo"))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> registerService.register(email, "foo"))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void testUserEnable(RootProvider<Data> rootProvider,
                        RegisterService registerService,
                        IdGenerator idGenerator,
                        EmailConfirmationRepository emailConfirmationRepository) throws UserAlreadyExistsException {
        String email = idGenerator.generate() + "@projectcheckins.org";
        String id = registerService.register(email, "password");
        assertThat(rootProvider.root().getUsers()).noneMatch(UserEntity::enabled);
        emailConfirmationRepository.enableByEmail(email);
        assertThat(rootProvider.root().getUsers()).anyMatch(UserEntity::enabled);

    }
}
