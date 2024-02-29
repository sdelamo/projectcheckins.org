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
