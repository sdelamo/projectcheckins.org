package org.projectcheckins.security.http;

import io.micronaut.security.authentication.AuthenticationResponse;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class LoginFailureUtilsTest {

    @Test
    void testNoFailure() {
        String loginFailure = "/foo";
        assertThat(LoginFailureUtils.loginFailure(loginFailure, AuthenticationResponse.success("foo@example.com")))
            .hasValueSatisfying(x -> assertThat(x).isEqualTo(URI.create(loginFailure)));
    }
}