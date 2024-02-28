package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserFetcher;

@MicronautTest(startApplication = false)
class EclipseStoreUserFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";


    @Test
    void authoritiesFetcher(UserFetcher userFetcher, RegisterService registerService) throws UserAlreadyExistsException {
        assertThat(userFetcher.findByEmail(NOT_FOUND_EMAIL))
            .isEmpty();
        registerService.register(FOUND_EMAIL, "password");
        assertThat(userFetcher.findByEmail(FOUND_EMAIL)).hasValueSatisfying(userState -> assertThat(userState)
            .matches(u -> u.getEmail().equals(FOUND_EMAIL))
            .matches(u -> u.getId() != null)
            .matches(u -> u.getPassword() != null && !u.getPassword().equals("password")));
    }
}