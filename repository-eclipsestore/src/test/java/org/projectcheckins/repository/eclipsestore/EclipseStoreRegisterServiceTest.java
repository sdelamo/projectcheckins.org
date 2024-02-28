package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.UserAlreadyExistsException;

@MicronautTest(startApplication = false)
class EclipseStoreRegisterServiceTest {
    @Test
    void testRegister(EclipseStoreRegisterService registerService) {
        String email = "sergio@projectcheckins.org";
        assertThatCode(() -> registerService.register(email, "foo"))
            .doesNotThrowAnyException();
        assertThatThrownBy(() -> registerService.register(email, "foo"))
            .isInstanceOf(UserAlreadyExistsException.class);
    }
}
