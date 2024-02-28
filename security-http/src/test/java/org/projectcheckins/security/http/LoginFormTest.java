package org.projectcheckins.security.http;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class LoginFormTest {
    @Test
    void usernameIsRequired(Validator validator) {
        assertFalse(validator.validate(new LoginForm(null, "rawpassword")).isEmpty());
        assertFalse(validator.validate(new LoginForm("", "rawpassword")).isEmpty());
        assertTrue(validator.validate(new LoginForm("manolo", "rawpassword")).isEmpty());
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertFalse(validator.validate(new LoginForm("manolo", null)).isEmpty());
        assertFalse(validator.validate(new LoginForm("manolo", "")).isEmpty());
    }

    @Test
    void loginFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(LoginForm.class)));
    }

    @Test
    void loginFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(LoginForm.class)));
    }

}