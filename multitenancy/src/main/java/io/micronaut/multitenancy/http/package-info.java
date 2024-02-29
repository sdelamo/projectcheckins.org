/**
 * Classes related with Multitenancy and the HTTP layer.
 * @author Sergio del Amo
 */
@Requires(classes = HttpRequest.class)
@Configuration
package io.micronaut.multitenancy.http;

import io.micronaut.context.annotation.Configuration;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;