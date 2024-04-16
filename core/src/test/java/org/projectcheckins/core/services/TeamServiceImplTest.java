package org.projectcheckins.core.services;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserFetcher;
import org.projectcheckins.security.UserState;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Property(name = "spec.name", value = "TeamServiceImplTest")
@MicronautTest(startApplication = false)
class TeamServiceImplTest {

    static final Profile USER_1 = new ProfileRecord(
            "user1",
            "user1@email.com",
            TimeZone.getDefault(),
            MONDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
            Format.MARKDOWN,
            null,
            null
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
            return "secret";
        }
    };

    @Inject
    TeamServiceImpl teamService;

    @Test
    void testFindAll() {
        assertThat(teamService.findAll(null))
                .isEqualTo(List.of(USER_1));
    }

    @Test
    void testSave() throws UserAlreadyExistsException {
        final TeamMemberSave form = new TeamMemberSave("user2@email.com");
        final String id = teamService.save(form, null);
        assertThat(id)
                .isEqualTo("xxx");
    }

    @Test
    void testSaveInvalidEmail() throws UserAlreadyExistsException {
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
                .hasMessage("save.form: Team member already registered.");
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    static class RegisterServiceMock implements RegisterService {
        @Override
        public String register(String email, String rawPassword, List<String> authorities) throws UserAlreadyExistsException {
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
    @Replaces(ProfileRepository.class)
    static class ProfileRepositoryMock extends SecondaryProfileRepository {
        @Override
        public List<? extends Profile> list(Tenant tenant) {
            return List.of(USER_1);
        }
    }
}
