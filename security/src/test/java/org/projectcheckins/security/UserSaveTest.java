package org.projectcheckins.security;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class UserSaveTest {
    @Test
    void usernameIsRequired(Validator validator) {
        assertFalse(validator.validate(new UserSave(null, "encodedpassword", Collections.emptyList() )).isEmpty());
        assertFalse(validator.validate(new UserSave("", "encodedpassword",  Collections.emptyList())).isEmpty());
        assertTrue(validator.validate(new UserSave("manolo", "encodedpassword",  Collections.emptyList())).isEmpty());
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertFalse(validator.validate(new UserSave("manolo", null, Collections.emptyList() )).isEmpty());
        assertFalse(validator.validate(new UserSave("manolo", "",  Collections.emptyList())).isEmpty());
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(UserSave.class)));
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(UserSave.class)));
    }

}