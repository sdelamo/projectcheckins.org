package org.projectcheckins.security.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class LoginFormTest {

    public static final String EMAIL = "manolo@projectcheckins.org";

    @Test
    void usernameIsRequired(Validator validator) {
        assertThat(validator.validate(new LoginForm(null, "rawpassword")))
            .isNotEmpty();
        assertThat(validator.validate(new LoginForm("", "rawpassword")))
                .anyMatch(x -> x.getPropertyPath().toString().equals("username"))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must not be blank");

        assertThat(validator.validate(new LoginForm("foo", "rawpassword")))
                .anyMatch(x -> x.getPropertyPath().toString().equals("username"))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must be a well-formed email address");
        assertThat(validator.validate(new LoginForm(EMAIL, "rawpassword")))
            .isEmpty();
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertThat(validator.validate(new LoginForm(EMAIL, null)))
            .isNotEmpty();
        assertThat(validator.validate(new LoginForm(EMAIL, "")))
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