package io.micronaut.multitenancy.http;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import jakarta.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link MultitenancyFilterConfiguration}.
 *
 * @author Sergio del Amo
 */

@ConfigurationProperties(MultitenancyFilterConfigurationProperties.PREFIX)
public class MultitenancyFilterConfigurationProperties implements MultitenancyFilterConfiguration {
    public static final String PREFIX = "micronaut.multitenancy.filter";
    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    /**
     *
     * The pattern the {@link MultitenancyFilter} should match.
     */
    @NonNull
    @NotBlank
    private String pattern = "/**";

    private boolean enabled = DEFAULT_ENABLED;

    /**
     * @return true if you want to enable the {@link MultitenancyFilter}
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    @NonNull
    public String getPattern() {
        return this.pattern;
    }

    /**
     * Enables {@link MultitenancyFilter}. Default value {@value #DEFAULT_ENABLED}
     * @param enabled True if it is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Pattern the {@link MultitenancyFilter} should match. Default value `/**`. URLS NOT MATCHED BY PREVIOUS PATTERN ARE NOT SECURED
     * @param pattern The pattern
     */
    public void setPath(@NonNull String pattern) {
        if (StringUtils.isNotEmpty(pattern)) {
            this.pattern = pattern;
        }
    }
}
