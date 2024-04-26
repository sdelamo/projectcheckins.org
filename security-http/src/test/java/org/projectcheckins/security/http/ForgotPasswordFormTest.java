package org.projectcheckins.security.http;

import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class ForgotPasswordFormTest {
    @Test
    void validation(Validator validator) {
        assertThat(validator.validate(new ForgotPasswordForm(null)))
                .hasNotBlankViolation("email");
        assertThat(validator.validate(new ForgotPasswordForm("foo")))
                .hasMalformedEmailViolation("email");
    }

    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(ForgotPasswordForm.class));
    }
}
