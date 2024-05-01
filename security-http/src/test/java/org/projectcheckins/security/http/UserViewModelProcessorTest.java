package org.projectcheckins.security.http;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class UserViewModelProcessorTest {

    @Inject
    BeanContext beanContext;

    @Test
    void securityViewModelProcessorIsDisabled() {
        assertTrue(beanContext.containsBean(UserViewModelProcessor.class));
        assertFalse(beanContext.containsBean(io.micronaut.views.model.security.SecurityViewModelProcessor.class));
    }
}
