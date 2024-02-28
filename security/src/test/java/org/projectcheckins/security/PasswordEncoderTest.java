package org.projectcheckins.security;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class PasswordEncoderTest {
    @Test
    void passwordEncoder(PasswordEncoder passwordEncoder) {
        String rawPassword = "admin123";
        assertThat(passwordEncoder.encode(rawPassword))
            .isNotEqualTo(rawPassword)
            .matches(x -> passwordEncoder.matches(rawPassword, x));
    }
}