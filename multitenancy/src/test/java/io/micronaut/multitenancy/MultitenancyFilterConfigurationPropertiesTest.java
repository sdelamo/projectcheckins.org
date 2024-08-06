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

package io.micronaut.multitenancy;

import io.micronaut.http.annotation.ServerFilter;
import io.micronaut.multitenancy.http.MultitenancyFilterConfigurationProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultitenancyFilterConfigurationPropertiesTest {

    @Test
    void defaultsToEnabled() {
        MultitenancyFilterConfigurationProperties properties = new MultitenancyFilterConfigurationProperties();
        assertTrue(properties.isEnabled());
    }

    @Test
    void defaultsToMatchAll() {
        MultitenancyFilterConfigurationProperties properties = new MultitenancyFilterConfigurationProperties();
        assertEquals(ServerFilter.MATCH_ALL_PATTERN, properties.getPattern());
    }

    @Test
    void canBeSet() {
        MultitenancyFilterConfigurationProperties properties = new MultitenancyFilterConfigurationProperties();
        properties.setEnabled(false);
        properties.setPath(""); // Empty has no effect
        assertFalse(properties.isEnabled());
        assertEquals(ServerFilter.MATCH_ALL_PATTERN, properties.getPattern());
        properties.setPath("/api/**");
        assertEquals("/api/**", properties.getPattern());
    }
}