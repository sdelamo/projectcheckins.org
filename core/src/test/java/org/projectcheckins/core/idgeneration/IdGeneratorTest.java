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

package org.projectcheckins.core.idgeneration;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

@MicronautTest
class IdGeneratorTest {

    @Test
    void testGenerate(IdGenerator idGenerator) {
        Set<String> ids = new HashSet<>();
        int expectedSize = 1000;
        for (int i = 0; i < expectedSize; i++) {
            String id = idGenerator.generate();
            ids.add(id);
        }
        assertThat(ids).hasSize(expectedSize);
    }
}
