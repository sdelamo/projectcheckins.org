package org.projectcheckins.bootstrap;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import static org.projectcheckins.test.ValidationAssert.*;

@MicronautTest(startApplication = false)
class BreadcrumbTest {

    @Test
    void labelNotNull(Validator validator) {
        assertThat(validator.validate(new Breadcrumb(null, null)))
                .hasNotNullViolation("label");
    }

    @Test
    void validBreadcrumb(Validator validator) {
        assertThat(validator.validate(new Breadcrumb(Message.of("Hello World"))))
                .isValid();
    }
}