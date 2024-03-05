package org.projectcheckins.security.constraints;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordMatchValidatorTest {
    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(PasswordMatchValidator.class));
    }

    @Test
    void passwordMatching() {
        PasswordMatchValidator validator = new PasswordMatchValidator();
        assertTrue(validator.isValid(new SignUpForm("admin", "admin123", "admin123"), null));
        assertFalse(validator.isValid(new SignUpForm("admin", "admin123", "foobar"), null));
        assertTrue(validator.isValid(new SignUpForm("admin", null, null), null));
        assertFalse(validator.isValid(new SignUpForm("admin", "admin123", null), null));
        assertFalse(validator.isValid(new SignUpForm("admin",  null, "admin123"), null));
    }

    @PasswordMatch
    @Serdeable
    public record SignUpForm(@NonNull @NotBlank @Email String email,
                             @NonNull @NotBlank String password,
                             @NonNull @NotBlank String repeatPassword) implements RepeatPasswordForm {

    }
}