package org.projectcheckins.security.services;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.*;
import org.projectcheckins.security.api.PublicProfile;
import org.projectcheckins.security.forms.TeamMemberSave;
import org.projectcheckins.security.repositories.PublicProfileRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Property(name = "spec.name", value = "TeamServiceImplTest")
@MicronautTest(startApplication = false)
class TeamServiceImplTest {

    static final PublicProfile USER_1 = new PublicProfileRecord(
            "user1",
            "user1@email.com",
            ""
    );

    static final UserState USER_1_STATE = new UserState() {
        @Override
        public String getId() {
            return USER_1.id();
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getEmail() {
            return USER_1.email();
        }

        @Override
        public String getPassword() {
            return "password";
        }
    };

    static final TeamInvitation INVITATION_1 = new TeamInvitationRecord("pending1@email.com", null);

    static final TeamInvitation INVITATION_2 = new TeamInvitationRecord("pending2@email.com", null);

    @Test
    void testFindPendingInvitations() {
        assertThat(teamService.findInvitations(null))
                .isEqualTo(List.of(INVITATION_1, INVITATION_2));
    }

    @Inject
    TeamServiceImpl teamService;

    @Test
    void testFindAll() {
        assertThat(teamService.findAll(null))
                .isEqualTo(List.of(USER_1));
    }

    @Test
    void testSave() {
        final TeamMemberSave form = new TeamMemberSave("user2@email.com");
        assertThatCode(() -> teamService.save(form, null))
                .doesNotThrowAnyException();
    }

    @Test
    void testSaveInvalidEmail() {
        final TeamMemberSave form = new TeamMemberSave("not an email");
        assertThatThrownBy(() -> teamService.save(form, null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("save.form.email: must be a well-formed email address");
    }

    @Test
    void testSaveAlreadyExists() {
        final TeamMemberSave form = new TeamMemberSave(USER_1.email());
        assertThatThrownBy(() -> teamService.save(form, null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("save.invitation: Invitation already exists");
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    static class RegisterServiceMock implements RegisterService {
        @Override
        public String register(String email, String rawPassword, Tenant tenant, List<String> authorities) throws RegistrationCheckViolationException {
            return "xxx";
        }
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    static class UserFetcherMock implements UserFetcher {

        @Override
        @NonNull
        public Optional<UserState> findById(@NotBlank @NonNull String id) {
            return Optional.empty();
        }

        @NonNull
        public Optional<UserState> findByEmail(@NotBlank @NonNull String email) {
            return USER_1_STATE.getEmail().equals(email) ? Optional.of(USER_1_STATE) : Optional.empty();
        }
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    static class ProfileRepositoryMock implements PublicProfileRepository {
        @Override
        public List<? extends PublicProfile> list(Tenant tenant) {
            return List.of(USER_1);
        }
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    @Replaces(TeamInvitationRepository.class)
    static class TeamInvitationRepositoryMock extends SecondaryTeamInvitationRepository {
        @Override
        public List<? extends TeamInvitation> findAll(@Nullable Tenant tenant) {
            return List.of(INVITATION_1, INVITATION_2);
        }

        @Override
        public void save(@NonNull @NotNull @Valid TeamInvitation invitation){
        }

        @Override
        public boolean existsByEmail(String email, @Nullable Tenant tenant) {
            return USER_1.email().equals(email);
        }
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    @Replaces(UserAlreadyExistsRegistrationCheck.class)
    static class UserAlreadyExistsRegistrationCheckMock extends UserAlreadyExistsRegistrationCheck {

        public UserAlreadyExistsRegistrationCheckMock(UserRepository userRepository) {
            super(userRepository);
        }

        @Override
        public Optional<RegistrationCheckViolation> validate(String email, Tenant tenant) {
            return Optional.empty();
        }
    }
    record PublicProfileRecord(String id, String email, String fullName) implements PublicProfile {
    }
}
