package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;

public interface Question {

    @NotBlank String id();

    @NotBlank String title();

    @NotBlank String schedule();
}
