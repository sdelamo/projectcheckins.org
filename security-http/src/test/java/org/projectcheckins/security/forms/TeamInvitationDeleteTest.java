package org.projectcheckins.security.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class TeamInvitationDeleteTest {

    final static Argument<TeamInvitationDelete> ARGUMENT = Argument.of(TeamInvitationDelete.class);

    @Test
    void emailIsRequired(Validator validator) {
        assertThat(validator.validate(new TeamInvitationDelete(null)))
                .hasNotBlankViolation("email");
        assertThat(validator.validate(new TeamInvitationDelete("")))
                .hasNotBlankViolation("email");
        assertThat(validator.validate(new TeamInvitationDelete("example@projectcheckins.org")))
                .isValid();
    }

    @Test
    void teamMemberDeleteIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serde) {
        assertThatCode(() -> serde.getDeserializableIntrospection(ARGUMENT))
                .doesNotThrowAnyException();
    }

    @Test
    void teamMemberDeleteIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serde) {
        assertThatCode(() -> serde.getSerializableIntrospection(ARGUMENT))
                .doesNotThrowAnyException();
    }
}
