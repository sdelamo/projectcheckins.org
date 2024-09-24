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

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.multitenancy.tenantresolver.FixedTenantResolverConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.multitenancy.tenantresolver.httpheader.enabled", value = StringUtils.TRUE)
@MicronautTest
@Property(name = "spec.name", value = "TenantNotResolvedTest")
class TenantNotResolvedTest {

    @Test
    void tenantIsResolvedAndBound(@Client("/") HttpClient client) {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve("/tenant/echo"));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, thrown.getStatus());
    }

    @Requires(property = "spec.name", value = "TenantNotResolvedTest")
    @Controller("/tenant")
    static class TenantResolvedController {

        @Produces(MediaType.TEXT_PLAIN)
        @Get("/echo")
        HttpResponse<?> index(@Nullable Tenant tenant) {
            return tenant != null ? HttpResponse.ok(tenant.id()) : HttpResponse.unprocessableEntity();
        }
    }
}
