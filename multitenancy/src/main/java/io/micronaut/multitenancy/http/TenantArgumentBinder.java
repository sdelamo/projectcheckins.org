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
