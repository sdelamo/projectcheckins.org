package org.projectcheckins.repository.eclipsestore;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.*;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreUserFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";

    @Inject
    TeamInvitationRepository teamInvitationRepository;

    @Test
    void authoritiesFetcher(UserFetcher userFetcher, RegisterService registerService) throws RegistrationCheckViolationException {
        assertThat(userFetcher.findByEmail(NOT_FOUND_EMAIL))
            .isEmpty();
        teamInvitationRepository.save(new TeamInvitationRecord(FOUND_EMAIL, null));
        registerService.register(FOUND_EMAIL, "password", null);
        assertThat(userFetcher.findByEmail(FOUND_EMAIL)).hasValueSatisfying(userState -> assertThat(userState)
                .matches(u -> !u.isEnabled())
                .matches(u -> u.getEmail().equals(FOUND_EMAIL))
                .matches(u -> u.getId() != null)
                .matches(u -> u.getPassword() != null && !u.getPassword().equals("password")));
    }

    @Test
    void testRegister(RegisterService registerService) {
        String email = "sergio@projectcheckins.org";
        String notInvited = "not-invited@projectcheckins.org";
        teamInvitationRepository.save(new TeamInvitationRecord(email, null));
        assertThatCode(() -> registerService.register(email, "foo", null))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> registerService.register(notInvited, "foo", null))
                .isInstanceOf(RegistrationCheckViolationException.class);
        assertThatThrownBy(() -> registerService.register(email, "foo", null))
                .isInstanceOf(RegistrationCheckViolationException.class);
    }

    @Test
    void testUserEnable(RootProvider<Data> rootProvider,
                        RegisterService registerService,
                        IdGenerator idGenerator,
                        EmailConfirmationRepository emailConfirmationRepository) throws RegistrationCheckViolationException {
        String email = idGenerator.generate() + "@projectcheckins.org";
        teamInvitationRepository.save(new TeamInvitationRecord(email, null));
        String id = registerService.register(email, "password", null);
        assertThat(rootProvider.root().getUsers()).noneMatch(UserEntity::enabled);
        emailConfirmationRepository.enableByEmail(email);
        assertThat(rootProvider.root().getUsers()).anyMatch(UserEntity::enabled);

    }
}
