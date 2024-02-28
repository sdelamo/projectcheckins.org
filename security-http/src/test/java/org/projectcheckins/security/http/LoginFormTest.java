package org.projectcheckins.security.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class LoginFormTest {
    @Test
    void usernameIsRequired(Validator validator) {
        assertThat(validator.validate(new LoginForm(null, "rawpassword")))
            .isNotEmpty();
        assertThat(validator.validate(new LoginForm("", "rawpassword")))
            .anyMatch(x -> x.getPropertyPath().toString().equals("username") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new LoginForm("manolo", "rawpassword")))
            .isEmpty();
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertThat(validator.validate(new LoginForm("manolo", null)))
            .isNotEmpty();
        assertThat(validator.validate(new LoginForm("manolo", "")))
            .anyMatch(x -> x.getPropertyPath().toString().equals("password") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void loginFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(LoginForm.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void loginFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(LoginForm.class)))
            .doesNotThrowAnyException();
    }

}