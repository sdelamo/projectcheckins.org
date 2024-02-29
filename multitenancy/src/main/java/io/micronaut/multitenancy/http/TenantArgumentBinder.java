package io.micronaut.multitenancy.http;

import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.bind.binders.TypedRequestArgumentBinder;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;

/**
 * @author Sergio del Amo
 */
@Singleton
public class TenantArgumentBinder implements TypedRequestArgumentBinder<Tenant> {
    @Override
    public Argument<Tenant> argumentType() {
        return Argument.of(Tenant.class);
    }

    @Override
    public BindingResult<Tenant> bind(ArgumentConversionContext<Tenant> context, HttpRequest<?> source) {
        if (!source.getAttributes().contains(MultitenancyFilter.KEY)) {
            @SuppressWarnings("unchecked") // Because all Micronaut provides is a raw BindingResult
            final BindingResult<Tenant> unsatisfied = BindingResult.UNSATISFIED;
            return unsatisfied;
        }
        return () -> source.getAttribute(MultitenancyFilter.TENANT, Tenant.class);
    }
}
