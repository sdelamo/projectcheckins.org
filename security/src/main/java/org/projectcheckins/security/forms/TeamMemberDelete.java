package org.projectcheckins.security.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record TeamMemberDelete(@NonNull @NotBlank @InputHidden String email) { }
