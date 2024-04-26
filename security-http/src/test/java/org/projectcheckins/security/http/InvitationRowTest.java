package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.type.Argument;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.*;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "InvitationRowTest")
class InvitationRowTest {
    final static Argument<InvitationRow> ARGUMENT = Argument.of(InvitationRow.class);

    @Test
    void validation(Validator validator) {
        assertThat(validator.validate(new InvitationRow("example@projectcheckins.org", null)))
                .isValid();
        assertThat(validator.validate(new InvitationRow(null, null)))
                .hasNotNullViolation("email");
        assertThat(validator.validate(new InvitationRow("email", null)))
                .hasMalformedEmailViolation("email");
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
