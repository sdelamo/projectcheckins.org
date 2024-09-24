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