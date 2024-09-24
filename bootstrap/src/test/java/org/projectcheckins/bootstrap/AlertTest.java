// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.bootstrap;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import io.micronaut.views.fields.messages.Message;
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

    @Test
    void alertInfo() {
        assertThat(Alert.info(Message.of("Hello")))
                .matches(alert -> alert.variant().equals(AlertVariant.INFO));

        assertThat(Alert.info(Message.of("Hello"), false))
                .matches(alert -> alert.variant().equals(AlertVariant.INFO) && !alert.dismissible());
    }

    @Test
    void alertDanger() {
        assertThat(Alert.danger(Message.of("Hello")))
                .matches(alert -> alert.variant().equals(AlertVariant.DANGER));

        assertThat(Alert.danger(Message.of("Hello"), false))
                .matches(alert -> alert.variant().equals(AlertVariant.DANGER) && !alert.dismissible());
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