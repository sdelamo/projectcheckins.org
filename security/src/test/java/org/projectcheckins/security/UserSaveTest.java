package org.projectcheckins.security;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest(startApplication = false)
class UserSaveTest {
    @Test
    void usernameIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave(null, "encodedpassword", emptyList())))
            .isNotEmpty();
        assertThat(validator.validate(new UserSave("", "encodedpassword", emptyList())))
            .anyMatch(x -> x.getPropertyPath().toString().equals("email") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new UserSave("manolo", "encodedpassword", emptyList())))
            .isEmpty();
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave("manolo", null, emptyList())))
            .isNotEmpty();
        assertThat(validator.validate(new UserSave("manolo", "", emptyList())))
            .anyMatch(x -> x.getPropertyPath().toString().equals("encodedPassword") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(UserSave.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(UserSave.class)))
            .doesNotThrowAnyException();
    }

}