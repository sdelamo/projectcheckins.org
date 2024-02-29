package io.micronaut.multitenancy.http;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.RequestFilter;
import io.micronaut.http.annotation.ServerFilter;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.multitenancy.exceptions.TenantNotFoundException;
import io.micronaut.multitenancy.tenantresolver.TenantResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Populates the current tenant, if resolved, in the request attribute {@value #TENANT}.
 * @author Sergio del Amo
 */
@Requires(property = MultitenancyFilterConfigurationProperties.PREFIX + ".enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
@Requires(bean = TenantResolver.class)
@ServerFilter("${micronaut.security.filter.pattern:/**}")
class MultitenancyFilter {
    private static final Logger LOG = LoggerFactory.getLogger(MultitenancyFilter.class);
    private final TenantResolver tenantResolver;
    public static final String KEY = "io.micronaut.multitenancy." + MultitenancyFilter.class.getSimpleName();
    public static final CharSequence TENANT = "micronaut.tenant";

    MultitenancyFilter(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @RequestFilter
    public void filterRequest(HttpRequest<?> request) {
        try {
            request.getAttributes().put(KEY, true);
            Serializable tenant = tenantResolver.resolveTenantIdentifier();
            request.setAttribute(TENANT, new Tenant(tenant.toString()));
        } catch (TenantNotFoundException e) {
            LOG.trace("Tenant not found for request {} {}", request.getMethod(), request.getPath());
        }
    }
}
