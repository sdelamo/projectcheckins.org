package org.projectcheckins.bootstrap;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest(startApplication = false)
class AlertTest {
    @Test
    void messageCannotBeNull(Validator validator) {
        assertThat(validator.validate(Alert.danger(null)))
                .isNotEmpty();
        String message = null;
        assertThat(validator.validate(new Alert(message, AlertVariant.DANGER, true)))
                .isNotEmpty();
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