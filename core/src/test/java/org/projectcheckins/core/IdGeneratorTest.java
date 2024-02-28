package org.projectcheckins.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.idgeneration.IdGenerator;

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
