package org.projectcheckins.bootstrap;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record Breadcrumb(@NotNull @Valid Message label, @Nullable String href) {
    public Breadcrumb(Message label) {
        this(label, null);
    }
}