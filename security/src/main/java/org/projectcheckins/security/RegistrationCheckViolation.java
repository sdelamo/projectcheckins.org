package org.projectcheckins.security;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.constraints.NotNull;

@Introspected
public record RegistrationCheckViolation(@NotNull Message message) {
}
