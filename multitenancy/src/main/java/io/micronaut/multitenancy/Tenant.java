package io.micronaut.multitenancy;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a tenant.
 * @author Sergio del Amo
 */
public record Tenant(@NotBlank String id) {
}
