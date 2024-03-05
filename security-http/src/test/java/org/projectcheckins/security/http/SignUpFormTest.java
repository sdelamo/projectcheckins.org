package org.projectcheckins.security.http;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class SignUpFormTest {

    @Test
    void emailCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new SignUpForm("", "secret", "secret")).isEmpty());
        assertFalse(validator.validate(new SignUpForm("", "secret", "secret")).isEmpty());
    }

    @Test
    void passwordAndRepeatPasswordCannotBeBlank(Validator validator) {
        assertFalse(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "", "")).isEmpty());
        assertFalse(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "", "")).isEmpty());
    }

    @Test
    void passwordAndRepeatPasswordShouldMatch(Validator validator) {
        assertFalse(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "secret", "foobar")).isEmpty());
    }

    @Test
    void matchingPasswordsAndEmailisValid(Validator validator) {
        assertTrue(validator.validate(new SignUpForm("sergio.delamo@softamo.com", "secret", "secret")).isEmpty());
    }

    @Test
    void signupFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(SignUpForm.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void signupFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(SignUpForm.class)))
                .doesNotThrowAnyException();
    }
}