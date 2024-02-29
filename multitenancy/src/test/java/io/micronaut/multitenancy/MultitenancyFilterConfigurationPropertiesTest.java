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