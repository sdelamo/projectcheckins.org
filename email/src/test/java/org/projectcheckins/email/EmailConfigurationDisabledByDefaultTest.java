package org.projectcheckins.email;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
class EmailConfigurationDisabledByDefaultTest {

    @Inject
    BeanContext beanContext;

    @Test
    void beanOfTypeEmailConfigurationDoesNotExist() {
        assertThat(beanContext.containsBean(EmailConfiguration.class)).isFalse();
    }

}
