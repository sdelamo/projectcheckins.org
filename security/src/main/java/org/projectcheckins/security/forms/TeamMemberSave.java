package org.projectcheckins.security.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record TeamMemberSave(@NonNull @NotBlank @Email String email) { }
