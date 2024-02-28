package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserFetcher;
import org.projectcheckins.security.UserState;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreUserFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";


    @Test
    void authoritiesFetcher(UserFetcher userFetcher, RegisterService registerService) throws UserAlreadyExistsException {
        assertTrue(userFetcher.findByEmail(NOT_FOUND_EMAIL).isEmpty());
        registerService.register(FOUND_EMAIL, "password");
        Optional<UserState> userStateOptional = userFetcher.findByEmail(FOUND_EMAIL);
        assertFalse(userStateOptional.isEmpty());
        UserState userState = userStateOptional.get();
        assertEquals(FOUND_EMAIL, userState.getEmail());
        assertNotNull(userState.getId());
        assertNotNull(userState.getPassword());
        assertNotEquals("password", userState.getPassword());
    }
}