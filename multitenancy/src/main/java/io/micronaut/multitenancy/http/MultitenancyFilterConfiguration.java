package io.micronaut.multitenancy.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.Toggleable;

/**
 * Configuration for {@link MultitenancyFilter}.
 * @author Sergio del Amo
 */
public interface MultitenancyFilterConfiguration extends Toggleable {

    /**
     *
     * @return The pattern the {@link MultitenancyFilter} should match.
     */
    @NonNull
    String getPattern();
}
