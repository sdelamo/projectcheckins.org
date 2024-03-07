package org.projectcheckins.bootstrap;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import static org.projectcheckins.test.ValidationAssert.*;
import static org.assertj.core.api.Assertions.*;

@Property(name = "spec.name", value = "AlertTest")
@MicronautTest(startApplication = false)
class AlertTest {
    @Test
    void messageCannotBeNull(Validator validator, AlertTestValidator testValidator) {
        assertThat(validator.validate(Alert.danger(null)))
                .hasNotNullViolation("message");
        String message = null;
        assertThatThrownBy(() -> testValidator.validate(new Alert(message, AlertVariant.DANGER, true)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Requires(property = "spec.name", value = "AlertTest")
    @Singleton
    static class AlertTestValidator {
        void validate(@Valid Alert alert) {
            // ...
        }
    }

    @Test
    void alertIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Alert.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void alertIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Alert.class)))
                .doesNotThrowAnyException();
    }

}