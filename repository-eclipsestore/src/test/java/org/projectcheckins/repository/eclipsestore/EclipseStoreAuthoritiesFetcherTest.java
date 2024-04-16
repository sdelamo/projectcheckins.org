package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.AuthoritiesFetcher;
import org.projectcheckins.security.RegistrationCheckViolationException;
import org.projectcheckins.security.UserSave;

import java.util.Collections;

@MicronautTest(startApplication = false)
class EclipseStoreAuthoritiesFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";

    private final static String FOUND_EMAIL_WITH_ROLES = "pepito@unityfoundation.io";

    @Test
    void authoritiesFetcher(AuthoritiesFetcher authoritiesFetcher, EclipseStoreUser registerService) throws RegistrationCheckViolationException {
        assertThat(authoritiesFetcher.findAuthoritiesByEmail(NOT_FOUND_EMAIL))
            .isEmpty();
        registerService.register(new UserSave(FOUND_EMAIL, "password", Collections.emptyList()), null);
        assertThat(authoritiesFetcher.findAuthoritiesByEmail(FOUND_EMAIL))
            .isEmpty();
        registerService.register(new UserSave(FOUND_EMAIL_WITH_ROLES, "password", Collections.singletonList("ROLE_USER")), null);
        assertThat(authoritiesFetcher.findAuthoritiesByEmail(FOUND_EMAIL_WITH_ROLES))
            .containsExactly("ROLE_USER");

    }
}