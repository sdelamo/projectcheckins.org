package org.projectcheckins.security.http;

import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class ResetPasswordFormTest {
    @Test
    void validation(Validator validator) {
        assertThat(validator.validate(new ResetPasswordForm(null, null, null)))
                .hasNotBlankViolation("token")
                .hasNotBlankViolation("password")
                .hasNotBlankViolation("repeatPassword");
    }

    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(ResetPasswordForm.class));
    }
}
