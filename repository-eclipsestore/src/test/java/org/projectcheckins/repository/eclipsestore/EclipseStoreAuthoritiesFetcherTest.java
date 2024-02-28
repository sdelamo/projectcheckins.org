package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.AuthoritiesFetcher;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreAuthoritiesFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";

    private final static String FOUND_EMAIL_WITH_ROLES = "pepito@unityfoundation.io";

    @Test
    void authoritiesFetcher(AuthoritiesFetcher authoritiesFetcher, RegisterService registerService) throws UserAlreadyExistsException {
        assertTrue(authoritiesFetcher.findAuthoritiesByEmail(NOT_FOUND_EMAIL).isEmpty());
        registerService.register(FOUND_EMAIL, "password");
        assertTrue(authoritiesFetcher.findAuthoritiesByEmail(NOT_FOUND_EMAIL).isEmpty());
        registerService.register(FOUND_EMAIL_WITH_ROLES, "password", Collections.singletonList("ROLE_USER"));
        assertFalse(authoritiesFetcher.findAuthoritiesByEmail(FOUND_EMAIL_WITH_ROLES).isEmpty());

    }
}