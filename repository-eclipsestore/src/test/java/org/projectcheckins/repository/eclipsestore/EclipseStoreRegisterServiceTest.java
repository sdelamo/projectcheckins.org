package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.UserAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(startApplication = false)
class EclipseStoreRegisterServiceTest {
    @Test
    void testRegister(EclipseStoreRegisterService registerService) {
        String email = "sergio@projectcheckins.org";
        assertDoesNotThrow(() -> registerService.register(email, "foo"));
        assertThrows(UserAlreadyExistsException.class, () -> registerService.register(email, "foo"));
    }
}
