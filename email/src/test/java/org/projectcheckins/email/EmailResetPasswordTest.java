package org.projectcheckins.email;

import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class EmailResetPasswordTest {
    @Test
    void validation(Validator validator) {
        assertThat(validator.validate(new EmailResetPassword(null, null)))
                .hasNotBlankViolation("email")
                .hasNotBlankViolation("url");
        assertThat(validator.validate(new EmailResetPassword("foo", null)))
                .hasMalformedEmailViolation("email")
                .hasNotBlankViolation("url");
    }

    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(EmailResetPassword.class));
    }
}